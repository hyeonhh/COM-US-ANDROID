package com.example.com_us.base.viewmodel

import androidx.lifecycle.ViewModel

abstract class BaseViewModel  : ViewModel(){
    protected inline val TAG: String
        get() = this::class.java.simpleName
}