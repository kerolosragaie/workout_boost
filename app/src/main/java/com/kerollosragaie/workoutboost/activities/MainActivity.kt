package com.kerollosragaie.workoutboost.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.kerollosragaie.workoutboost.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.flStart.setOnClickListener {
            val intent = Intent(this@MainActivity,ExerciseActivity::class.java)
            startActivity(intent)
        }
    }
}