package com.example.com_us.ui.question.block

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.activity.OnBackPressedCallback
import com.example.com_us.databinding.ActivityQuestionCollectBlockBinding
import com.example.com_us.ui.question.result.ResultAfterSignActivity
import com.example.com_us.util.ThemeType
import dagger.hilt.android.AndroidEntryPoint

// 대화 블럭 획득 화면
@AndroidEntryPoint
class CollectBlockActivity : AppCompatActivity() {

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
            val intent = Intent(this, ResultAfterSignActivity::class.java)
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