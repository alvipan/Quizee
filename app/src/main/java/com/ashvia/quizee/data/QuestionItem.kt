package com.ashvia.quizee.data

data class QuestionItem(
    var content: String = "",
    var options: ArrayList<String> = ArrayList(),
    var answer: Int = 0,
    var reward: Int = 0,
    var timer: Long = 15
)
