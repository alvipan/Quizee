package com.ashvia.quizee.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ashvia.quizee.data.Question
import com.ashvia.quizee.data.QuestionResult
import com.ashvia.quizee.data.User

class MainViewModel: ViewModel() {
    var user = MutableLiveData<User>()
    var question = MutableLiveData<Question>()
    var questionId = MutableLiveData<String>()
    var questionResult = MutableLiveData<QuestionResult>()
}