package com.example.com_us.ui.home.theme

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.com_us.R
import com.example.com_us.databinding.ActivityThemeQuestionListBinding

class ThemeQuestionListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityThemeQuestionListBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityThemeQuestionListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setActionBar()
    }

    private fun setActionBar() {
        setSupportActionBar(binding.includeToolbar.toolbar)

        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_left)
    }
}