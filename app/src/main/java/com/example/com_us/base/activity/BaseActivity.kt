package com.example.com_us.base.activity

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.example.com_us.base.viewmodel.BaseViewModel

abstract class BaseActivity<B : ViewBinding, VM : BaseViewModel>(
    private val inflate : (LayoutInflater) -> B,
): AppCompatActivity(){

    protected lateinit var binding: B
        private set

    protected abstract val viewModel: VM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = inflate(layoutInflater)
        setContentView(binding.root)
        onBindLayout()
    }

    protected open fun onBindLayout() = Unit
}