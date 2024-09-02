package com.example.com_us.ui.question

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
import androidx.lifecycle.MutableLiveData
import com.example.com_us.data.response.question.ResponseAnswerDetailDto
import com.example.com_us.databinding.DialogQuestionFollowAlongBinding
import com.example.com_us.util.QuestionManager

private const val ARG_PARAM_QUESTION  = "paramQuestion"
private const val ARG_PARAM_ANSWER = "paramAnswer"
private const val ARG_PARAM_CATEGORY = "paramCategory"
private const val ARG_PARAM_SIGNDATA = "paramSignData"

class QuestionFollowAlongDialog : DialogFragment(), MoveActivityInterface {
    // TODO: Rename and change types of parameters
    private lateinit var paramQuestion: String
    private lateinit var paramAnswer: String
    private lateinit var paramCategory: String

    private lateinit var signData: List<ResponseAnswerDetailDto>

    private var videoPlayCount: MutableLiveData<Int> = MutableLiveData(0)

    private var _binding: DialogQuestionFollowAlongBinding? = null
    private val binding get() = _binding!!

    private val questionViewModel: QuestionViewModel by viewModels { QuestionViewModelFactory(requireContext()) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        questionViewModel.questionInterface = this
        arguments?.let {
            paramQuestion = it.getString(ARG_PARAM_QUESTION)!!
            paramAnswer = it.getString(ARG_PARAM_ANSWER)!!
            paramCategory = it.getString(ARG_PARAM_CATEGORY)!!
            //paramSignData = it.getSerializable(ARG_PARAM_SIGNDATA) as List<ResponseAnswerDetailDto>
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DialogQuestionFollowAlongBinding.inflate(inflater, container, false)
        val view = binding.root
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        signData = QuestionManager.signLanguageInfo

        setAnswerDetail()
        getAnswerDate()

        binding.buttonAnswerComplete.setOnClickListener {
            val questionId = QuestionManager.questionId
            if(questionId > 0 && paramAnswer.isNotEmpty()) questionViewModel.postAnswer(questionId, paramAnswer)
        }

        // Inflate the layout for this fragment
        return view
    }

    override fun onStart() {
        super.onStart()

        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    companion object {
        @JvmStatic
        fun newInstance(paramQuestion: String, paramAnswer: String, paramCategory: String) =
            QuestionFollowAlongDialog().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM_QUESTION, paramQuestion)
                    putString(ARG_PARAM_ANSWER, paramAnswer)
                    putString(ARG_PARAM_CATEGORY, paramCategory)
                    //putSerializable(ARG_PARAM_SIGNDATA, ArrayList(paramSignData))
                }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setAnswerDetail() {
        binding.textviewFollowdialogQuestion.text = paramQuestion
        var repeatCount = 0

        videoPlayCount.observe(this) {
            if(it >= 0) setSignDetail(it)
        }

        if(signData.size > 1) {
            binding.videoviewFollowdialogSign.setOnCompletionListener {
                if(videoPlayCount.value!! >= signData.size-1){
                    if(++repeatCount > 3) {
                        videoPlayCount.value = -1
                        repeatCount = 0
                    } else {
                        videoPlayCount.value = 0
                    }
                } else
                    videoPlayCount.value = videoPlayCount.value?.plus(1)

            }
        }
    }

    private fun getAnswerDate() {
        questionViewModel.resultData.observe(this) {
            if(it != null) QuestionManager.answerDate = it.answerDate
        }
    }

    private fun setSignDetail(signIdx: Int) {
        binding.textviewFollowdialogAnswer.text = signData[signIdx].signLanguageName
        binding.videoviewFollowdialogSign.setVideoURI(Uri.parse(signData[signIdx].signLanguageVideoUrl))
        binding.videoviewFollowdialogSign.start()
        binding.textviewFollodialogDescrp.text = signData[signIdx].signLanguageDescription
    }

    override fun moveToBlockCollect() {
        val intent = Intent(activity, QuestionCollectBlockActivity::class.java)
        intent.putExtra("category", paramCategory)
        startActivity(intent)
        activity?.finish()
    }
}