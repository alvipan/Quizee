package com.ashvia.quizee

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel: ViewModel() {
    val text = MutableLiveData<String>().apply {
        value = "This is notifications Fragment"
    }
}