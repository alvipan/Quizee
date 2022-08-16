package com.ashvia.quizee.model

data class User(
    var uid: String,
    var username: String,
    var email: String,
    var photoUrl: String,
    var point: Double = 0.0
)
