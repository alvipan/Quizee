package com.ashvia.quizee.ui.answer

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.ashvia.quizee.MainActivity
import com.ashvia.quizee.R
import com.ashvia.quizee.databinding.FragmentQuestionResultDialogBinding
import com.ashvia.quizee.ui.main.MainViewModel

class QuestionResultDialogFragment : DialogFragment() {

    private lateinit var binding: FragmentQuestionResultDialogBinding
    private val model: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentQuestionResultDialogBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        model.questionResult.observe(viewLifecycleOwner) {
            binding.questionCount.text = it.count.toString()
            binding.correctCount.text = it.correct.toString()
            binding.wrongCount.text = it.wrong.toString()
            binding.pointCollected.text = it.point.plus(it.bonus).toString()
        }

        binding.confirmButton.setOnClickListener {
            (activity as MainActivity).navController.popBackStack(R.id.navigation_question_overview,true)
            dismiss()
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.setCanceledOnTouchOutside(false)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return dialog
    }
}