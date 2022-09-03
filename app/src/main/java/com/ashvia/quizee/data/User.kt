package com.ashvia.quizee.data

import java.io.Serializable

data class User(
    var uid: String? = "",
    var displayName: String? = "",
    var email: String? = "",
    var photoUrl: String? = "",
    var point: Int? = 0,
    var material: ArrayList<String>? = ArrayList(),
    var role: String? = "member"
): Serializable