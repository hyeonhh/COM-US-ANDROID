package com.example.com_us.ui.question

import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import com.example.com_us.R
import com.example.com_us.data.response.question.ResponseAnswerDetailDto
import com.example.com_us.databinding.ActivityQuestionCheckAnswerBinding
import com.example.com_us.util.QuestionManager


class QuestionCheckAnswerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQuestionCheckAnswerBinding
    private val questionViewModel: QuestionViewModel by viewModels { QuestionViewModelFactory(this) }

    private lateinit var answer: String
    private lateinit var question: String
    private lateinit var category: String
    private lateinit var signData: List<ResponseAnswerDetailDto>

    private var videoPlayCount: MutableLiveData<Int> = MutableLiveData(0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        answer = intent.getStringExtra("answer").toString()
        question = intent.getStringExtra("question").toString()
        category = intent.getStringExtra("category").toString()

        if(!answer.isNullOrEmpty()){
            QuestionManager.question = question
            questionViewModel.loadAnswerDetail(answer)
        }

        binding = ActivityQuestionCheckAnswerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setActionBar()
        questionViewModel.answerDetail.observe(this) {
            if(!it.isNullOrEmpty()){
                QuestionManager.signLanguageInfo = it
                signData = it
                setAnswerDetail()
                binding.buttonAnswerFollowalong.setOnClickListener {
                    moveToFollowAlongDialog()
                }
            }
        }
    }

    private fun setActionBar() {
        setSupportActionBar(binding.includeAnswerToolbar.toolbar)

        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setHomeAsUpIndicator(R.drawable.ic_x)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }


    private fun setAnswerDetail() {
        binding.textviewAnswerQuestion.text = question
        var repeatCount = 0

        videoPlayCount.observe(this) {
            if(it >= 0) setSignDetail(it)
        }

        if(signData.size > 1) {
            binding.videoviewAnswerSign.setOnCompletionListener {
                if(videoPlayCount.value!! >= signData.size-1){
                    if(++repeatCount > 3) {
                        videoPlayCount.value = -1
                        repeatCount = 0
                    } else {
                        videoPlayCount.value = 0
                    }
                } else if(videoPlayCount.value != -1)
                    videoPlayCount.value = videoPlayCount.value?.plus(1)

            }
        }
    }

    private fun setSignDetail(signIdx: Int) {
        binding.textviewAnswerAnswer.text = signData[signIdx].signLanguageName
        binding.videoviewAnswerSign.setVideoURI(Uri.parse(signData[signIdx].signLanguageVideoUrl))
        binding.videoviewAnswerSign.start()
        binding.textviewAnswerDescrp.text = signData[signIdx].signLanguageDescription
    }
    private fun moveToFollowAlongDialog() {
        videoPlayCount.value = -1
        val dialog = QuestionFollowAlongDialog.newInstance(question, answer, category)
        dialog.show(supportFragmentManager, "FollowAlongDialog")
    }
}