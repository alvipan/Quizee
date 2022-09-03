package com.ashvia.quizee

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.ashvia.quizee.data.User
import com.ashvia.quizee.databinding.ActivityMainBinding
import com.ashvia.quizee.ui.main.MainViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var binding: ActivityMainBinding

    lateinit var navController: NavController

    private val model: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth
        database = Firebase.database

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.content) as NavHostFragment
        navController = navHostFragment.navController
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_dashboard,
                R.id.navigation_question,
                R.id.navigation_leaderboard,
                R.id.navigation_account,
                R.id.navigation_question_answer
            )
        )

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.navigation_question_overview,
                R.id.navigation_question_material-> {
                    binding.navView.apply {
                        animate()
                            .translationY(this.height.toFloat()).duration = 500
                    }
                }
                R.id.navigation_question_answer -> {
                    binding.navView.apply {
                        animate()
                            .translationY(this.height.toFloat()).duration = 500
                    }
                }
                else -> {
                    binding.navView.apply {
                        animate()
                            .translationY(0f).duration = 500
                    }
                }
            }
        }

        binding.toolbar.setupWithNavController(navController, appBarConfiguration)
        binding.navView.setupWithNavController(navController)
        getUserData()
    }

    private fun getUserData() {
        val ref = database.getReference("users").child(auth.uid!!)
        val refListener = object: ValueEventListener {
            override fun onCancelled(error: DatabaseError) {}
            override fun onDataChange(snapshot: DataSnapshot) {
                model.user.value = snapshot.getValue<User>()
            }
        }
        ref.addValueEventListener(refListener)
    }
}