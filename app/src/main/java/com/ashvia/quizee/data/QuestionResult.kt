package com.ashvia.quizee.data

data class QuestionResult(
    var count: Int = 0,
    var correct: Int = 0,
    var wrong: Int = 0,
    var point: Int = 0,
    var bonus: Int = 0
)
