package com.example.com_us.ui.question.select.conversation

import android.content.Context
import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.View
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Row
import androidx.compose.ui.platform.ViewCompositionStrategy
import com.example.com_us.R
import com.example.com_us.base.activity.BaseActivity
import com.example.com_us.databinding.ActivityConversationQuestionBinding
import com.example.com_us.ui.compose.AnswerTypeTag
import com.example.com_us.ui.compose.QuestionTypeTag
import com.example.com_us.ui.question.previous.PreviousAnswerActivity
import com.example.com_us.ui.question.select.selection.SelectAnswerActivity
import com.example.com_us.util.ColorMatch
import com.example.com_us.util.QuestionManager
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class ConversationQuestionActivity : BaseActivity<ActivityConversationQuestionBinding, ConversationQuestionViewModel>(
    ActivityConversationQuestionBinding::inflate
){
    private var type : String? = null
    private var answerType : String? = null
    private var questionId: Long = -1
    override val viewModel : ConversationQuestionViewModel by viewModels()

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
        questionId = intent.getLongExtra("questionId", 0L)
        type = intent.getStringExtra("type")
        answerType = intent.getStringExtra("answerType")
        Timber.d("answerTYpe:$answerType")
        return super.onCreateView(name, context, attrs)

    }


    private fun setQuestionTypeCompose(category : String) {
        binding.questionCategory.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                Row {
                    //todo :데이터 연결
                    val colorType = ColorMatch.fromKor(category)
                    if (colorType != null) {
                        QuestionTypeTag(colorType.colorType, category)
                    }
                }
            }
        }
    }

    private fun setAnswerTypeCompose() {
        binding.answerType.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                Row {
                    //todo :데이터 연결
                    val colorType = ColorMatch.fromKor("대화형")
                    if (colorType != null) {
                        AnswerTypeTag(colorType.colorType, "대화형")
                    }
                }
            }
        }
    }

    private fun setPreviousAnswerButton() {
        binding.buttonDetailAnswerbefore.setOnClickListener {
            val intent = Intent(this, PreviousAnswerActivity::class.java)
            QuestionManager.questionId = questionId
            intent.putExtra("questionId", questionId)
            startActivity(intent)
        }
    }


    override fun onBindLayout() {
        super.onBindLayout()

        // 이전 답변 보기 기능
        binding.buttonDetailAnswerbefore.setOnClickListener {
            setPreviousAnswerButton()
        }

        if (answerType=="BOTH") {
            binding.goToSelect.visibility = View.VISIBLE
        }
        binding.editText.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s!= null)
                if (s.isNotEmpty()){
                        val resource =
                            when (type) {
                                "DAILY" -> resources.getDrawable(R.drawable.answer_button_daily)
                                "INTEREST" -> resources.getDrawable(R.drawable.answer_button_interest)
                                "FAMILY" -> resources.getDrawable(R.drawable.answer_button_family)
                                "FRIEND" -> resources.getDrawable(R.drawable.answer_button_friend)
                                "SCHOOL" -> resources.getDrawable(R.drawable.answer_button_school)
                                else -> resources.getDrawable(R.drawable.answer_button_daily)
                            }
                        binding.buttonDetailComplete.setBackgroundDrawable(resource)
                        binding.buttonDetailComplete.setTextColor(resources.getColor(R.color.white))
                }
                else {
                    binding.buttonDetailComplete.setBackgroundDrawable(resources.getDrawable(R.drawable.shape_fill_rect10_gray200))
                    binding.buttonDetailComplete.setTextColor(resources.getColor(R.color.gray_500))
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })

        type?.let { setQuestionTypeCompose( it) }
        setAnswerTypeCompose()

        binding.goToSelect.setOnClickListener {
            val intent = Intent(this, SelectAnswerActivity::class.java)
            intent.putExtra("answerType", "BOTH")
            intent.putExtra("type",type)
            intent.putExtra("questionId", questionId)
            startActivity(intent)
        }
    }
}