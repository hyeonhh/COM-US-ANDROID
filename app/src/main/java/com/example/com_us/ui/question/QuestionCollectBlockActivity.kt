package com.example.com_us.ui.question

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.activity.OnBackPressedCallback
import com.example.com_us.R
import com.example.com_us.databinding.ActivityQuestionCollectBlockBinding
import com.example.com_us.util.ThemeType

class QuestionCollectBlockActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQuestionCollectBlockBinding
    private lateinit var category: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuestionCollectBlockBinding.inflate(layoutInflater)
        setContentView(binding.root)

        category = intent.getStringExtra("category").toString()

        if(!category.isNullOrEmpty()){
            setTheme()
        }

        controlBackButton()

        Handler().postDelayed({
            val intent = Intent(this, QuestionResultActivity::class.java)
            startActivity(intent)
            finish()
        }, 1500) // 3초
    }

    private fun setTheme() {
        binding.textviewCollectTheme.text = category
        val blockRes = ThemeType.findResFromKor(category)
        if(blockRes != null){
            binding.imageviewCollectBlockicon.setImageResource(blockRes)
        }
    }

    private fun controlBackButton() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
            }
        })
    }

}