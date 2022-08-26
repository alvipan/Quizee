package com.ashvia.quizee.model

data class Question(
    var title: String? = "Tanpa Judul",
    var category: String? = "Uncategory",
    var material: Material? = Material(),
    var items: ArrayList<QuestionItem>? = ArrayList(),
)
