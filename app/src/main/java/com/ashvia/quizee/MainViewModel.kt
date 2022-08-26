package com.ashvia.quizee

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ashvia.quizee.model.User

class MainViewModel: ViewModel() {
    var user = MutableLiveData<User>()
}