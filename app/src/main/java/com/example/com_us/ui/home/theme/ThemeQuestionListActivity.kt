package com.example.com_us.ui.home.theme

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.example.com_us.R
import com.example.com_us.databinding.ActivityThemeQuestionListBinding

class ThemeQuestionListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityThemeQuestionListBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityThemeQuestionListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val theme = intent.getStringExtra("theme")
        val themeKor = intent.getStringExtra("themeKor")
        binding.textviewTitle.text = String.format(resources.getString(R.string.theme_question_list_title), themeKor)

        setActionBar()
    }

    private fun setActionBar() {
        setSupportActionBar(binding.includeToolbar.toolbar)

        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_left)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}