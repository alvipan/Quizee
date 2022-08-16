package com.ashvia.quizee.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {

    private val text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
}