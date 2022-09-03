package com.ashvia.quizee.ui.answer

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ashvia.quizee.data.Question

class AnswerViewModel: ViewModel() {
    var questionId = MutableLiveData<String>()
    var question = MutableLiveData<Question>()
}