package com.ashvia.quizee.ui.editor

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.ashvia.quizee.R
import com.ashvia.quizee.adapter.QuestionEditorAdapter
import com.ashvia.quizee.databinding.FragmentQuestionEditorBinding

class QuestionEditorFragment : Fragment() {

    private lateinit var binding: FragmentQuestionEditorBinding
    private val model: EditorViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentQuestionEditorBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val questionSheet = QuestionEditorDialogFragment()
        val addButton = view.findViewById<Button>(R.id.addButton)
        addButton.setOnClickListener {
            questionSheet.show(parentFragmentManager,null)
        }

        val questionItemsAdapter = QuestionEditorAdapter()
        binding.questionItems.adapter = questionItemsAdapter
        binding.questionItems.layoutManager = LinearLayoutManager(context)

        model.question.observe(viewLifecycleOwner) {
            questionItemsAdapter.dataSet.clear()
            for (item in it.items!!) {
                questionItemsAdapter.dataSet.add(item)
                questionItemsAdapter.notifyDataSetChanged()
            }
        }
    }
}