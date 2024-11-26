package com.example.com_us.ui.question.result

import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.com_us.R
import com.example.com_us.base.activity.BaseActivity
import com.example.com_us.data.model.question.response.question.ResponseAnswerDetailDto
import com.example.com_us.databinding.ActivityQuestionCheckAnswerBinding
import com.example.com_us.ui.base.UiState
import com.example.com_us.ui.question.sign.SignAnswerDialog
import com.example.com_us.util.QuestionManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

// 답변 선택 시 처음으로 이동하는 화면 (질문, 답변 , 수형 영상, 따라해보기 버튼)
@AndroidEntryPoint
class ResultBeforeSignActivity : BaseActivity<ActivityQuestionCheckAnswerBinding,ResultViewModel>(
    ActivityQuestionCheckAnswerBinding::inflate
){

    override val viewModel: ResultViewModel by viewModels()

    private lateinit var answer: String
    private lateinit var question: String
    private lateinit var category: String
    private lateinit var signData: List<ResponseAnswerDetailDto>

    private var videoPlayCount: MutableLiveData<Int> = MutableLiveData(0)


    override fun onBindLayout() {
        super.onBindLayout()
        answer = intent.getStringExtra("answer").toString()
        question = intent.getStringExtra("question").toString()
        category = intent.getStringExtra("category").toString()

        if (answer.isNotEmpty()) {
            QuestionManager.question = question
            viewModel.loadAnswerDetail(answer)
        }


        setActionBar()

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.answerDetail.collect {
                    when (it) {
                        is UiState.Success -> {
                            QuestionManager.signLanguageInfo = it.data
                            signData = it.data
                            setAnswerDetail()
                            binding.buttonAnswerFollowalong.setOnClickListener {
                                moveToFollowAlongDialog()
                            }
                        }

                        is UiState.Error -> {
                            Toast.makeText(this@ResultBeforeSignActivity, it.toString(), Toast.LENGTH_SHORT).show()
                        }
                        is UiState.Loading -> {}
                    }
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

        viewModel.answerDetail.value
        viewModel.answerDetail.value.let {
            when(it) {
                is UiState.Success -> {
                    val dialog =
                    SignAnswerDialog.newInstance(question, answer, category,
                        it.data
                    )
                       dialog.isCancelable = false
                        dialog.show(supportFragmentManager, "FollowAlongDialog")

                }
                else -> {
                    Toast.makeText(this , "잠시 후에 다시 시도해주세요",Toast.LENGTH_SHORT).show()
                }
            }
        }

        print(viewModel.answerDetail.value)

        // 다이얼로그가 뜨면 아래 내용 질문만 희미하게 보이게 하기
        binding.textView8.visibility = View.GONE
        binding.textviewAnswerAnswer.visibility = View.GONE
        binding.textView13.visibility = View.GONE
        binding.textviewAnswerDescrp.visibility = View.GONE
        binding.buttonAnswerFollowalong.visibility = View.GONE
        binding.videoviewAnswerSign.visibility = View.GONE

    }

}