package com.example.com_us.ui.question.result

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.lifecycle.MutableLiveData
import com.example.com_us.MainActivity
import com.example.com_us.R
import com.example.com_us.data.model.question.response.question.ResponseAnswerDetailDto
import com.example.com_us.databinding.ActivityQuestionResultBinding
import com.example.com_us.util.QuestionManager
import dagger.hilt.android.AndroidEntryPoint

// 수형 따라해보기 완료 후 대화 블럭 얻기 전 답변 완료 화면
@AndroidEntryPoint
class ResultAfterSignActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQuestionResultBinding

    private var videoPlayCount: MutableLiveData<Int> = MutableLiveData(0)

    private lateinit var signData: List<ResponseAnswerDetailDto>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuestionResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(!QuestionManager.signLanguageInfo.isNullOrEmpty()){
            signData = QuestionManager.signLanguageInfo
            setAnswerDetail()
        }

        if(!QuestionManager.answerDate.isNullOrEmpty()) {
            binding.textviewResultDate.text = QuestionManager.answerDate
        }

        setActionBar()
        setCompleteButton()
    }

    private fun setActionBar() {
        binding.includeResultToolbar.textviewToolbarTitle.text = resources.getString(R.string.question_result_toolbar_title)
        setSupportActionBar(binding.includeResultToolbar.toolbar)

        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_left)
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
        binding.textviewResultQuestion.text = QuestionManager.question
        var repeatCount = 0

        videoPlayCount.observe(this) {
            if(it >= 0) setSignDetail(it)
        }

        if(signData.size > 1) {
            binding.videoviewResultSign.setOnCompletionListener {
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
        binding.textviewResultAnswer.text = signData[signIdx].signLanguageName
        binding.videoviewResultSign.setVideoURI(Uri.parse(signData[signIdx].signLanguageVideoUrl))
        binding.videoviewResultSign.start()
        binding.textviewResultDescrp.text = signData[signIdx].signLanguageDescription
    }

    private fun setCompleteButton() {
        binding.buttonResultComplete.setOnClickListener{
            QuestionManager.reset()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

}