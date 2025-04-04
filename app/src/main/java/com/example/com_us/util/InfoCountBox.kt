package com.example.com_us.util

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.TypedArrayUtils.getResourceId
import com.example.com_us.R
import com.example.com_us.databinding.ViewInfoCountBoxBinding

class InfoCountBox
    @JvmOverloads
    constructor(
        context: Context,
        attrs: AttributeSet,
        defStyleAttr: Int = 0,
    ) : ConstraintLayout(context, attrs, defStyleAttr) {
        private lateinit var titleTextView: TextView
        private lateinit var countTextView: TextView
        private lateinit var iconImageView: ImageView

        private lateinit var binding : ViewInfoCountBoxBinding

        init {
            initializeView()
            getAttrs(attrs, defStyleAttr)
        }

        fun setAnswerCount(count : Int){
            binding.txtCount.text = "${count}개"
        }


    private fun initializeView(){
        val layoutInflater = LayoutInflater.from(context)
        binding = ViewInfoCountBoxBinding.inflate(layoutInflater,this,true)
        titleTextView = binding.txtTitle
        countTextView = binding.txtCount
        iconImageView = binding.ivIcon
    }

        private fun getAttrs(
            attrs: AttributeSet?,
            defStyleAttr: Int,
        ) {
            val typedArray =
                context.obtainStyledAttributes(
                    attrs,
                    R.styleable.InfoCountBox,
                    defStyleAttr,
                    0,
                )
            try {
                typedArray.getString(R.styleable.InfoCountBox_customTitleTextView)?.let {
                    titleTextView.text = it
                }
                typedArray.getString(R.styleable.InfoCountBox_customCountTextView)?.let {
                    countTextView.text = it
                }
                val countTextColor =
                    typedArray.getColor(
                        R.styleable.InfoCountBox_customCountTextColor,
                        Color.rgb(255, 157, 10),
                    )

                countTextView.setTextColor(countTextColor)

                val iconResId =
                    typedArray.getResourceId(
                        R.styleable.InfoCountBox_customCountIcon,
                        -1,
                    )

                if (iconResId != -1) {
                    iconImageView.setImageResource(iconResId)
                }
            } finally {
                typedArray.recycle()
            }
        }
    }
