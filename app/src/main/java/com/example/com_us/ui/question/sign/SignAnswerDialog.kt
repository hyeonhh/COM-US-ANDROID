package com.example.com_us.ui.question.sign

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.com_us.data.model.question.response.question.ResponseAnswerDetailDto
import com.example.com_us.databinding.DialogQuestionFollowAlongBinding
import com.example.com_us.ui.question.block.CollectBlockActivity
import com.example.com_us.ui.question.result.ResultAfterSignActivity
import com.example.com_us.util.QuestionManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

// 수형 따라해보기 화면
@AndroidEntryPoint
class SignAnswerDialog(
    private val question : String,
    private val answer : String,
    private val category : String,
    private val signDescription: List<ResponseAnswerDetailDto>,
) : DialogFragment() {
    private lateinit var signData: List<ResponseAnswerDetailDto>
    private var index = 0

    private var _binding: DialogQuestionFollowAlongBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SignAnswerViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogQuestionFollowAlongBinding.inflate(inflater, container, false)
        val view = binding.root
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        signData = signDescription
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setSignDetail(0)


        binding.btnCompleteWithoutBlock.setOnClickListener {
            val intent = Intent(context,ResultAfterSignActivity::class.java)
            startActivity(intent)
        }
        binding.buttonNextStep.setOnClickListener {
            if (viewModel.signIndex.value == signData.lastIndex) {
                val intent = Intent(context,CollectBlockActivity::class.java)
                intent.putExtra("category",category)
                startActivity(intent)
            }
            else {
                index +=1
                viewModel.setSignIndex(index)
                setSignDetail(index)
            }

        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.signIndex.collect{
                    binding.linearProgressIndicator.progress = (100 / signData.size) * (it + 1)
                }
            }
        }

        binding.textviewFollowdialogQuestion.text = question
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    companion object {
        fun newInstance(paramQuestion: String, paramAnswer: String, paramCategory: String,signDescription: List<ResponseAnswerDetailDto>) =
            SignAnswerDialog(paramQuestion,paramAnswer,paramCategory, signDescription).apply {
                arguments = Bundle()
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



    // todo: 역할이 뭐지?
    private fun getAnswerDate() {
        viewModel.resultData.observe(this) {
            if(it != null) QuestionManager.answerDate = it.answerDate
        }
    }

    private fun setSignDetail(signIdx: Int) {
        if (signIdx > signData.lastIndex) return
        if (viewModel.signIndex.value == signData.lastIndex) {
            binding.buttonNextStep.text = "완료하기"
            binding.btnCompleteWithoutBlock.visibility = View.GONE
            viewModel.postAnswer(QuestionManager.questionId, answer)
        }
        binding.textviewFollowdialogAnswer.text = signData[signIdx].signLanguageName
        binding.videoviewFollowdialogSign.setVideoURI(Uri.parse(signData[signIdx].signLanguageVideoUrl))
        binding.videoviewFollowdialogSign.start()
        binding.textviewFollodialogDescrp.text = signData[signIdx].signLanguageDescription
    }
}