package com.ashvia.quizee.ui.answer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.ashvia.quizee.databinding.FragmentQuestionMaterialBinding
import com.ashvia.quizee.ui.main.MainViewModel
import com.google.android.gms.ads.AdRequest

class QuestionMaterialFragment : Fragment() {

    private lateinit var binding: FragmentQuestionMaterialBinding
    private val model: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentQuestionMaterialBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adRequest = AdRequest.Builder().build()
        binding.adView.loadAd(adRequest)
        model.question.observe(viewLifecycleOwner) {
            binding.materialTitle.text = it.title
            binding.materialContent.loadData(it.material?.content!!,"text/html","utf-8")
        }
    }
}