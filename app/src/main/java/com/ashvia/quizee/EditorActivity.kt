package com.ashvia.quizee

import android.os.Bundle
import android.view.Menu
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.ashvia.quizee.databinding.ActivityEditorBinding
import com.ashvia.quizee.model.Question
import com.ashvia.quizee.ui.editor.DetailEditorFragment
import com.ashvia.quizee.ui.editor.EditorViewModel
import com.ashvia.quizee.ui.editor.MaterialEditorFragment
import com.ashvia.quizee.ui.editor.QuestionEditorFragment
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class EditorActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditorBinding
    private lateinit var database: FirebaseDatabase
    private val model: EditorViewModel by viewModels()
    private var question = Question()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = binding.toolbar
        toolbar.title = "Editor"
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.save_question -> {
                    pushQuestion()
                    true
                }
                else -> false
            }
        }

        database = Firebase.database
        database.getReference("config/question/category").get().addOnSuccessListener {
            model.category.value = it.getValue<List<String>>()
        }
        model.question.observe(this) {
            question = it
        }

        val adapter = supportFragmentManager.let {
            this.ViewPagerAdapter(it, lifecycle)
        }
        binding.viewPager.adapter = adapter
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = "Rincian"
                1 -> tab.text = "Soal"
                2 -> tab.text = "Materi"
            }
        }.attach()
    }

    private fun pushQuestion() {
        database.getReference("question").push().setValue(question)
        finish()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.editor_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    inner class ViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
        FragmentStateAdapter(fragmentManager, lifecycle) {

        override fun getItemCount(): Int {
            return 3
        }

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> DetailEditorFragment()
                1 -> QuestionEditorFragment()
                else -> MaterialEditorFragment()
            }
        }
    }
}