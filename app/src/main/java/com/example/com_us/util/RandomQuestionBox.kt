package com.example.com_us.util

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.com_us.R

// 랜덤 질문 박스
class RandomQuestionBox(
    context : Context,
    attrs : AttributeSet
)  : ConstraintLayout(context,attrs){

    init {
        inflate(context, R.layout.view_random_question_box, this)
    }
}