package com.ashvia.quizee.ui.editor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.ashvia.quizee.data.QuestionItem
import com.ashvia.quizee.databinding.FragmentQuestionEditorDialogBinding

class QuestionEditorDialogFragment : DialogFragment() {

    private var _binding: FragmentQuestionEditorDialogBinding? = null
    private val binding get() = _binding!!
    private val model: EditorViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentQuestionEditorDialogBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.saveButton.setOnClickListener {
            val questionContent = binding.questionContent.text.toString()
            val questionOption: ArrayList<String> = ArrayList()
            questionOption.add(binding.questionOptionA.text.toString())
            questionOption.add(binding.questionOptionB.text.toString())
            questionOption.add(binding.questionOptionC.text.toString())
            questionOption.add(binding.questionOptionD.text.toString())

            val answerCheckedId = binding.questionAnswer.checkedRadioButtonId
            val answerCheckedButton = binding.questionAnswer.findViewById<RadioButton>(answerCheckedId)
            val questionAnswer = binding.questionAnswer.indexOfChild(answerCheckedButton)

            // set default reward
            var questionReward = 0
            binding.questionReward.text.toString().let {
                if (it.isNotEmpty()) {
                    questionReward = it.toInt()
                }
            }

            // set default timer
            var questionTimer = 15L
            binding.questionTimer.text.toString().let {
                if (it.isNotEmpty()) {
                    questionTimer = it.toLong()
                }
            }

            val questionItem = QuestionItem(
                questionContent,
                questionOption,
                questionAnswer,
                questionReward,
                questionTimer
            )
            model.addItem(questionItem)
            this.dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}