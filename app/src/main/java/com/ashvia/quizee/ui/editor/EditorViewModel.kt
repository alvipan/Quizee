package com.ashvia.quizee.ui.editor

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ashvia.quizee.model.Material
import com.ashvia.quizee.model.Question
import com.ashvia.quizee.model.QuestionItem

class EditorViewModel: ViewModel() {

    var category = MutableLiveData<List<String>>().apply {
        postValue(ArrayList<String>())
    }
    var question = MutableLiveData<Question>().apply {
        this.postValue(Question())
    }

    fun setTitle(title: String?) {
        if (title != null) {
            val data = question.value
            data?.title = title
            question.value = data
        }
    }

    fun setCategory(category: String?) {
        val data = question.value
        data?.category = category
        question.value = data
    }

    fun setMaterial(material: Material) {
        val data = question.value
        data?.material = material
        question.value = data
    }

    fun addItem(item: QuestionItem) {
        val data = question.value
        data?.items?.add(item)
        question.value = data
    }
}
