package com.ashvia.quizee.ui.answer

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.CountDownTimer
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RadioButton
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.ashvia.quizee.R
import com.ashvia.quizee.data.QuestionItem
import com.ashvia.quizee.data.QuestionResult
import com.ashvia.quizee.databinding.FragmentAnswerQuestionBinding
import com.ashvia.quizee.ui.main.MainViewModel
import com.google.android.material.appbar.MaterialToolbar

class AnswerQuestionFragment : Fragment() {

    private lateinit var binding: FragmentAnswerQuestionBinding
    private lateinit var toolbar: MaterialToolbar
    private lateinit var cToolbar: View

    private val model: MainViewModel by activityViewModels()
    private var questionList = ArrayList<QuestionItem>()
    private var question = QuestionItem()
    private var result = QuestionResult()
    private var number = 0
    private var countDownTimer: CountDownTimer? = null

    @SuppressLint("InflateParams")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAnswerQuestionBinding.inflate(inflater, container, false)
        toolbar = requireActivity().findViewById(R.id.toolbar)

        val param = Toolbar.LayoutParams(
            Toolbar.LayoutParams.WRAP_CONTENT,
            Toolbar.LayoutParams.WRAP_CONTENT)
        param.gravity = Gravity.END

        cToolbar = inflater.inflate(R.layout.toolbar_question_answer,null)
        cToolbar.layoutParams = param
        toolbar.addView(cToolbar)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        model.question.observe(viewLifecycleOwner) {
            questionList = it.items!!
            question = questionList[number]
            result.count = questionList.size
            showQuestion()
        }

        binding.answerOption.setOnCheckedChangeListener { radioGroup, _ ->
            binding.confirm.isEnabled = radioGroup.checkedRadioButtonId != -1
        }

        binding.confirm.setOnClickListener {
            enableOption(false)
            if (binding.answerOption.checkedRadioButtonId != -1) {
                countDownTimer?.cancel()
                getResult()
            }
        }
        enableOption(false)
    }

    override fun onPause() {
        super.onPause()
        toolbar.subtitle = ""
        toolbar.removeView(cToolbar)
    }

    private fun enableOption(boolean: Boolean) {
        binding.answerOption[0].isEnabled = boolean
        binding.answerOption[1].isEnabled = boolean
        binding.answerOption[2].isEnabled = boolean
        binding.answerOption[3].isEnabled = boolean
        binding.confirm.isEnabled = false
    }

    private fun getCountdownTimer(): CountDownTimer {
        val timer = question.timer.times(1000)
        val progressBar = cToolbar.findViewById<ProgressBar>(R.id.progressBar)
        val progressTimer = cToolbar.findViewById<TextView>(R.id.progressTimer)

        return object : CountDownTimer(timer, 1000) {
            override fun onTick(ms: Long) {
                progressTimer.text = (ms/1000).toString()
            }
            override fun onFinish() {
                progressTimer.text = "0"
                enableOption(false)
                getResult()
            }
        }
    }

    private fun next() {
        clearSelection()
        if (number + 1 < questionList.size) {
            number++
            question = questionList[number]
            showQuestion()
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun clearSelection() {
        binding.answerOption.clearCheck()
        binding.answerOption[0].background = resources.getDrawable(R.drawable.radio_answer_option)
        binding.answerOption[1].background = resources.getDrawable(R.drawable.radio_answer_option)
        binding.answerOption[2].background = resources.getDrawable(R.drawable.radio_answer_option)
        binding.answerOption[3].background = resources.getDrawable(R.drawable.radio_answer_option)
    }

    private fun showQuestion() {
        toolbar.subtitle = String.format("%s dari %s", (number+1), questionList.size)
        binding.questionContent.text = question.content
        binding.radioButton.text = question.options[0]
        binding.radioButton2.text = question.options[1]
        binding.radioButton3.text = question.options[2]
        binding.radioButton4.text = question.options[3]

        countDownTimer = getCountdownTimer()
        countDownTimer?.start()
        enableOption(true)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun getResult() {
        val answerCheckedId = binding.answerOption.checkedRadioButtonId

        if (answerCheckedId != -1) {
            val answerCheckedButton = binding.answerOption.findViewById<RadioButton>(answerCheckedId)
            val checked = binding.answerOption.indexOfChild(answerCheckedButton)

            if (checked != question.answer) {
                result.wrong++
                answerCheckedButton.background = resources.getDrawable(R.drawable.badge_warning)
            } else {
                result.correct++
                result.point = result.point.plus(question.reward)
            }
            binding.answerOption[question.answer].background = resources.getDrawable(R.drawable.badge_success)
        } else {
            result.wrong++
        }

        model.questionResult.value = result

        object : CountDownTimer(2000, 1000) {
            override fun onTick(millisUntilFinished: Long) {}
            override fun onFinish() {
                if ((number+1) < questionList.size) {
                    next()
                } else {
                    showResultDialog()
                }
            }
        }.start()
    }

    private fun showResultDialog() {
        val resultDialog = QuestionResultDialogFragment()
        resultDialog.show(parentFragmentManager, "Result Dialog")
    }
}