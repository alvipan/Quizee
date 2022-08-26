package com.ashvia.quizee.model

import java.io.Serializable

data class User(
    var uid: String? = "",
    var displayName: String? = "",
    var email: String? = "",
    var photoUrl: String? = "",
    var point: Double = 0.0,
    var role: String = "member"
): Serializable
