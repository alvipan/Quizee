package com.ashvia.quizee.ui.answer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.ashvia.quizee.MainActivity
import com.ashvia.quizee.R
import com.ashvia.quizee.databinding.FragmentQuestionOverviewBinding
import com.ashvia.quizee.ui.main.MainViewModel
import com.google.android.gms.ads.AdRequest

class QuestionOverviewFragment : Fragment() {

    private lateinit var binding: FragmentQuestionOverviewBinding
    private val model: MainViewModel by activityViewModels()
    private  var materialCheckOutDialog: MaterialCheckOutDialogFragment? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentQuestionOverviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        model.question.observe(viewLifecycleOwner) {
            var questionPoint = 0
            var questionTimer = 0L
            for (item in it.items!!) {
                questionPoint += item.reward
                questionTimer += item.timer
            }

            binding.questionTitle.text = it.title
            binding.questionCount.text = it.items!!.size.toString()
            binding.questionPoint.text = String.format("%s Poin", questionPoint)
            binding.questionTimer.text = getTimer(questionTimer)
        }

        binding.buttonReadMaterial.setOnClickListener {
            model.user.value?.material?.let {
                if (it.contains(model.questionId.value)) {
                    (activity as MainActivity).navController.navigate(R.id.navigation_question_material)
                } else {
                    materialCheckOutDialog = MaterialCheckOutDialogFragment()
                    materialCheckOutDialog?.show(parentFragmentManager, null)
                }
            }
        }

        binding.buttonStart.setOnClickListener {
            (activity as MainActivity).navController.navigate(R.id.navigation_question_answer)
        }
        loadBannerAd()
    }

    private fun getTimer (timer: Long): String {
        var minutes = 0
        var seconds = timer
        while (seconds >= 60) {
            minutes++
            seconds -= 60
        }
        return when (minutes) {
            0 -> String.format("%s detik", seconds)
            else -> String.format("%sm %sd", minutes, seconds)
        }
    }

    private fun loadBannerAd() {
        val adRequest = AdRequest.Builder().build()
        binding.adView.loadAd(adRequest)
    }
}