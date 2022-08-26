package com.ashvia.quizee.model

data class QuestionItem(
    var content: String? = "",
    var options: ArrayList<String>? = ArrayList(),
    var answer: Int? = 0,
    var reward: Int? = 0,
    var timer: Int? = 15
)
