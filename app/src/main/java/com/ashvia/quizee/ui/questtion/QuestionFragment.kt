package com.ashvia.quizee.ui.questtion

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ashvia.quizee.EditorActivity
import com.ashvia.quizee.MainViewModel
import com.ashvia.quizee.R
import com.ashvia.quizee.adapter.QuestionAdapter
import com.ashvia.quizee.databinding.FragmentQuestionBinding
import com.ashvia.quizee.model.Question
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class QuestionFragment : Fragment() {

    private var _binding: FragmentQuestionBinding? = null
    private val binding get() = _binding!!
    private lateinit var database: FirebaseDatabase
    private lateinit var model: QuestionViewModel
    private lateinit var toolbar: Toolbar
    private var isAdmin = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentQuestionBinding.inflate(inflater, container, false)
        toolbar = activity?.findViewById(R.id.toolbar)!!

        val mainViewModel: MainViewModel by activityViewModels()
        if (mainViewModel.user.value?.role  == "admin") {
            isAdmin = true
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        database = Firebase.database
        model = ViewModelProvider(this)[QuestionViewModel::class.java]

        if (isAdmin) {
            toolbar.inflateMenu(R.menu.question_menu)
            toolbar.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.add_question -> {
                        val intent = Intent(context, EditorActivity::class.java)
                        startActivity(intent)
                        true
                    }
                    else -> false
                }
            }
        }
        getQuestionList()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        if (isAdmin) {
            toolbar.menu.clear()
        }
    }

    private fun getQuestionList() {
        val questionAdapter = QuestionAdapter()
        binding.questionList.layoutManager = LinearLayoutManager(context)
        binding.questionList.adapter = questionAdapter

        val ref = database.getReference("question")
        val listener = object: ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                questionAdapter.dataSet.clear()
                for (data in snapshot.children) {
                    data.getValue<Question>()?.let {
                        questionAdapter.dataSet.add(it)
                        questionAdapter.notifyDataSetChanged()
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        }
        ref.addValueEventListener(listener)
    }
}