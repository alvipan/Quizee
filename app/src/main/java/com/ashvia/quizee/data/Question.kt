package com.ashvia.quizee.data

import java.io.Serializable

data class Question(
    var title: String? = "",
    var category: String? = "",
    var material: Material? = Material(),
    var items: ArrayList<QuestionItem>? = ArrayList()
): Serializable