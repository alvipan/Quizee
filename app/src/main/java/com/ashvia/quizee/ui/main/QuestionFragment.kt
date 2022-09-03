package com.ashvia.quizee.ui.main

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.ashvia.quizee.EditorActivity
import com.ashvia.quizee.MainActivity
import com.ashvia.quizee.R
import com.ashvia.quizee.adapter.QuestionAdapter
import com.ashvia.quizee.data.Question
import com.ashvia.quizee.databinding.FragmentQuestionBinding
import com.google.android.material.appbar.MaterialToolbar
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class QuestionFragment : Fragment() {

    private lateinit var database: FirebaseDatabase
    private lateinit var binding: FragmentQuestionBinding
    private lateinit var toolbar: MaterialToolbar

    private val model: MainViewModel by activityViewModels()
    private var questionList: ArrayList<Question> = ArrayList()
    private var questionId: ArrayList<String> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentQuestionBinding.inflate(inflater, container, false)
        toolbar = requireActivity().findViewById(R.id.toolbar)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        database = Firebase.database
        getQuestionList()
    }

    override fun onResume() {
        super.onResume()
        getMenu()
    }

    override fun onPause() {
        super.onPause()
        toolbar.menu.clear()
    }

    private fun getMenu() {
        toolbar.inflateMenu(R.menu.question_menu)
        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.add_question -> {
                    startActivity(Intent(requireActivity(), EditorActivity::class.java))
                    true
                }
                else -> false
            }
        }
    }

    private fun getQuestionList() {
        val questionAdapter = QuestionAdapter(requireContext())
        binding.questionList.layoutManager = LinearLayoutManager(context)
        binding.questionList.adapter = questionAdapter

        val ref = database.getReference("question")
        val listener = object: ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                questionId.clear()
                questionList.clear()
                for (data in snapshot.children) {
                    data.key?.let { questionId.add(it) }
                    data.getValue<Question>()?.let { questionList.add(it) }
                }
                questionAdapter.dataSet = questionList
                questionAdapter.notifyDataSetChanged()
            }
            override fun onCancelled(error: DatabaseError) {}
        }
        ref.addValueEventListener(listener)

        questionAdapter.onItemClick = {
            model.questionId.value = questionId[it]
            model.question.value = questionList[it]
            (activity as MainActivity).navController.navigate(R.id.action_navigation_question_to_answer_navigation)
        }
    }
}