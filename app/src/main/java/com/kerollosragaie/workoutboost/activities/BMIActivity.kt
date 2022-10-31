package com.kerollosragaie.workoutboost.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.kerollosragaie.workoutboost.databinding.ActivityBmiBinding
import java.math.BigDecimal
import java.math.RoundingMode

///! Next dev BMI Calculator functionality

class BMIActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBmiBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBmiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        settingUpToolBar()

        //For bmi
        calculateBMI()
    }

    //*** For app toolbar
    private fun settingUpToolBar() {
        setSupportActionBar(binding.toolbarBMI)
        //*** shows top back button
        if (supportActionBar != null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        binding.toolbarBMI.setNavigationOnClickListener {
            onBackPressed()
        }
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    //*** Calculate BMI functionality
    private fun calculateBMI() {
        binding.btnCalculateUnits.setOnClickListener {
            if (validateMetricUnits()) {
                val heightValue: Float = binding.etMetricUnitHeight?.text.toString().toFloat() / 100
                val weightValue: Float = binding.etMetricUnitWeight?.text.toString().toFloat()
                val bmi = weightValue / (heightValue * heightValue)
                displayBmiResults(bmi)
            } else {
                Toast.makeText(this@BMIActivity, "Please enter valid values.", Toast.LENGTH_SHORT)
                    .show()
            }
        }

    }

    private fun displayBmiResults(bmi: Float) {
        val bmiLabel: String
        val bmiDescription: String

        if (bmi.compareTo(15f) <= 0) {
            bmiLabel = "Very severely underweight"
            bmiDescription = "Oops! You really need to take better care of yourself! Eat more!"
        } else if (bmi.compareTo(15f) > 0 && bmi.compareTo(16f) <= 0) {
            bmiLabel = "Severely underweight"
            bmiDescription = "Oops!You really need to take better care of yourself! Eat more!"
        } else if (bmi.compareTo(16f) > 0 && bmi.compareTo(18.5f) <= 0) {
            bmiLabel = "Underweight"
            bmiDescription = "Oops! You really need to take better care of yourself! Eat more!"
        } else if (bmi.compareTo(18.5f) > 0 && bmi.compareTo(25f) <= 0) {
            bmiLabel = "Normal"
            bmiDescription = "Congratulations! You are in a good shape!"
        } else if (bmi.compareTo(25f) > 0 && bmi.compareTo(30f) <= 0) {
            bmiLabel = "Overweight"
            bmiDescription = "Oops! You really need to take care of your yourself! Workout maybe!"
        } else if (bmi.compareTo(30f) > 0 && bmi.compareTo(35f) <= 0) {
            bmiLabel = "Obese Class | (Moderately obese)"
            bmiDescription = "Oops! You really need to take care of your yourself! Workout maybe!"
        } else if (bmi.compareTo(35f) > 0 && bmi.compareTo(40f) <= 0) {
            bmiLabel = "Obese Class || (Severely obese)"
            bmiDescription = "OMG! You are in a very dangerous condition! Act now!"
        } else {
            bmiLabel = "Obese Class ||| (Very Severely obese)"
            bmiDescription = "OMG! You are in a very dangerous condition! Act now!"
        }

        val bmiValue = BigDecimal(bmi.toDouble())
            .setScale(2, RoundingMode.HALF_EVEN).toString()

        //to view the BMI result
        binding.llDisplayBMIResult.visibility = View.VISIBLE

        binding.tvBMIValue.text = bmiValue
        binding.tvBMIType.text = bmiLabel
        binding.tvBMIDescription.text = bmiDescription
    }

    private fun validateMetricUnits(): Boolean {
        var isValid = true

        if (binding?.etMetricUnitWeight?.text.toString().isEmpty()) {
            isValid = false
        } else if (binding?.etMetricUnitHeight?.text.toString().isEmpty()) {
            isValid = false
        }

        return isValid
    }

}