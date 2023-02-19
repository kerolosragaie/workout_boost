package com.kerollosragaie.workoutboost.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.kerollosragaie.workoutboost.databinding.ActivityBmiBinding
import java.math.BigDecimal
import java.math.RoundingMode

class BMIActivity : AppCompatActivity() {
    //?Note: we can use the companion object without creating a new instance from class
    companion object {
        private const val METRIC_UNITS_VIEW = "METRIC_UNITS_VIEW"
        private const val US_UNITS_VIEW = "US_UNITS_VIEW"
    }

    private var currentVisibleView: String = METRIC_UNITS_VIEW

    private lateinit var binding: ActivityBmiBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBmiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        settingUpToolBar()

        //For bmi calculation
        calculateBMI()

        //For group buttons
        setupRadioGroupBttns()
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
            calculateUnits()
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

        if (binding.etMetricUnitWeight.text.toString().isEmpty()) {
            isValid = false
        } else if (binding.etMetricUnitHeight.text.toString().isEmpty()) {
            isValid = false
        }

        return isValid
    }

    private fun calculateUnits(){
        if(currentVisibleView == METRIC_UNITS_VIEW){
            if (validateMetricUnits()) {
                val heightValue: Float = binding.etMetricUnitHeight.text.toString().toFloat() / 100
                val weightValue: Float = binding.etMetricUnitWeight.text.toString().toFloat()
                val bmi = weightValue / (heightValue * heightValue)
                displayBmiResults(bmi)
            } else {
                Toast.makeText(this@BMIActivity, "Please enter valid values.", Toast.LENGTH_SHORT)
                    .show()
            }
        }else{
            if (validateUsUnits()) {
                val usUnitHeightValueFeet: Float = binding.etUsUnitHeightFeet.text.toString().toFloat()
                val usUnitHeightValueInch: Float = binding.etUsUnitHeightInch.text.toString().toFloat()
                val usUnitWeightValue: Float = binding.etUsUnitWeightPounds.text.toString().toFloat()
                val heightValue = usUnitHeightValueInch + usUnitHeightValueFeet * 12
                val bmi = 703 * (usUnitWeightValue / (heightValue * heightValue))
                displayBmiResults(bmi)
            } else {
                Toast.makeText(this@BMIActivity, "Please enter valid values.", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }
    private fun validateUsUnits(): Boolean {
        var isValid = true

        if (binding.etUsUnitWeightPounds.text.toString().isEmpty()) {
            isValid = false
        } else if (binding.etUsUnitHeightFeet.text.toString().isEmpty()) {
            isValid = false
        }else if (binding.etUsUnitHeightInch.text.toString().isEmpty()) {
            isValid = false
        }
        return isValid
    }

    //*** Units group buttons (show/hide)
    private fun setupRadioGroupBttns() {
        binding.rgUnits.setOnCheckedChangeListener { _, checkedId: Int ->
            currentVisibleView = if (checkedId == binding.rbMetricUnits.id) {
                METRIC_UNITS_VIEW
            } else {
                US_UNITS_VIEW
            }
            unitsGroupButtonsVisibility()
        }
    }

    private fun unitsGroupButtonsVisibility() {
        if (currentVisibleView == METRIC_UNITS_VIEW) {
            binding.clUsUnits.visibility = View.GONE
            binding.clMetricUnits.visibility = View.VISIBLE
            binding.llDisplayBMIResult.visibility = View.INVISIBLE
            currentVisibleView = METRIC_UNITS_VIEW
        } else {
            binding.clMetricUnits.visibility = View.GONE
            binding.clUsUnits.visibility = View.VISIBLE
            binding.llDisplayBMIResult.visibility = View.INVISIBLE
            currentVisibleView = US_UNITS_VIEW
        }

    }

}