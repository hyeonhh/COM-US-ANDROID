package com.example.com_us.ui.question

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.com_us.R
import com.example.com_us.databinding.ActivityQuestionAnswerBinding

class QuestionAnswerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQuestionAnswerBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityQuestionAnswerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setActionBar()
    }

    private fun setActionBar() {
        setSupportActionBar(binding.includeAnswerToolbar.toolbar)

        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setHomeAsUpIndicator(R.drawable.ic_x)
    }
}