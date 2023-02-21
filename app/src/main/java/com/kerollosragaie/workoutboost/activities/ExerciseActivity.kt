package com.kerollosragaie.workoutboost.activities

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.kerollosragaie.workoutboost.Constants
import com.kerollosragaie.workoutboost.R
import com.kerollosragaie.workoutboost.adapters.ExerciseStatusAdapter
import com.kerollosragaie.workoutboost.databinding.ActivityExerciseBinding
import com.kerollosragaie.workoutboost.databinding.DialogCustomLayoutBinding
import com.kerollosragaie.workoutboost.models.Exercise
import java.util.*
import kotlin.collections.ArrayList

class ExerciseActivity : AppCompatActivity(), TextToSpeech.OnInitListener {
    private lateinit var binding: ActivityExerciseBinding

    //***For rest countdown timer
    private var restCountDownTimer: CountDownTimer? = null
    private var restProgress: Int = 0

    //*** For exercise countdown timer
    private var exerciseCountDownTimer: CountDownTimer? = null
    private var exerciseProgress: Int = 0
    private var pauseOffset: Long = 0

    //*** Get exercises list
    private var exercisesList: ArrayList<Exercise>? = null
    private var currentExercisePosition = -1

    //*** Text to speech
    private lateinit var tts: TextToSpeech

    //*** Media player
    private lateinit var mediaPlayer: MediaPlayer

    //*** For showing the current exercise in progress
    private lateinit var exerciseAdapter: ExerciseStatusAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExerciseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        settingUpToolBar()

        setUpExercises()

        setUpRestCountDownTimer()

        settingUpTTS()

        setUpExerciseStatusRV()
    }

    //*** Setup exercise status RV
    private fun setUpExerciseStatusRV() {
        binding.rvExerciseStatus.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        exerciseAdapter = ExerciseStatusAdapter(exercisesList!!)

        binding.rvExerciseStatus.adapter = exerciseAdapter
    }

    //*** For app toolbar
    private fun settingUpToolBar() {
        setSupportActionBar(binding.toolbarExerciseActivity)
        //*** shows top back button
        if (supportActionBar != null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        binding.toolbarExerciseActivity.setNavigationOnClickListener {
            pauseCountDownTimer()
            customDialogShow()
        }
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    //*** for custom  dialog
    private fun customDialogShow() {
        val customDialog = Dialog(this)
        //? it is a separate xml file so it needs its own binding
        val dialogBinding = DialogCustomLayoutBinding.inflate(layoutInflater)
        customDialog.setContentView(dialogBinding.root)
        customDialog.setCanceledOnTouchOutside(false) // cannot close on touch outside of dialog

        dialogBinding.btnYes.setOnClickListener {
            this@ExerciseActivity.finish()
            customDialog.dismiss()
        }

        dialogBinding.btnNo.setOnClickListener {
            customDialog.dismiss()
            setExerciseProgressBar(pauseOffset+1000) //+1000 to prevent counter reach -1 (if +2000 so counter reaches -2)
        }

        customDialog.show()

    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        customDialogShow()
        //super.onBackPressed()  //no need to it as it will finish current activity
    }

    //*** For TTS
    private fun settingUpTTS() {
        tts = TextToSpeech(this, this)
    }

    private fun speakOut(text: String) {
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "")
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts.setLanguage(Locale.US)
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "The Language specified is not supported!")
            }
        } else {
            Log.e("TTS", "Initialization failed!")
        }
    }

    //*** For exercises
    private fun setUpExercises() {
        exercisesList = Constants.defaultExerciseList()
        currentExercisePosition = -1
    }

    //*** For rest countdown timer
    private fun setUpRestCountDownTimer() {
        if (restCountDownTimer != null) {
            restCountDownTimer?.cancel()
            restProgress = 0
        }
        setRestProgressBar()
    }

    private fun setRestProgressBar() {
        if (binding.restLayout.restLayout.visibility == View.GONE) {
            binding.restLayout.restLayout.visibility = View.VISIBLE
        }

        binding.restLayout.progressBar.progress = restProgress
        binding.restLayout.tvUpcomingExercise.text =
            exercisesList!![currentExercisePosition + 1].getName()

        //For media player to play sounds
        try {
            val soundURI = Uri.parse(
                "android.resource://com.kerollosragaie.workoutboost/" + R.raw.start_sound
            )
            mediaPlayer = MediaPlayer.create(applicationContext, soundURI)
            mediaPlayer.isLooping = false
            mediaPlayer.start()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        /**
         * @countDownInterval every 1 sec (1000 ms) onTick function works
         * */
        restCountDownTimer = object : CountDownTimer(1000, 1000) {
            override fun onTick(p0: Long) {
                restProgress++
                binding.restLayout.progressBar.progress = (exercisesList!![currentExercisePosition + 1].getRestingTime()) - restProgress
                binding.restLayout.tvTimer.text = ((exercisesList!![currentExercisePosition + 1].getRestingTime()) - restProgress).toString()
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun onFinish() {
                binding.restLayout.restLayout.visibility = View.GONE
                currentExercisePosition++ //Go to next exercise

                exercisesList!![currentExercisePosition].setIsSelected(true)
                //? Will call onBindViewHolder again with the new updated data
                exerciseAdapter.notifyDataSetChanged()

                setUpExerciseCountDownTimer()
            }
        }.start()
    }

    //*** For exercise countdown timer
    private fun setUpExerciseCountDownTimer() {
        if (exerciseCountDownTimer != null) {
            exerciseCountDownTimer?.cancel()
            exerciseProgress = 0
        }
        setExerciseProgressBar(pauseOffset)
    }

    @SuppressLint("SetTextI18n")
    private fun setExerciseProgressBar(pauseOffsetL: Long) {
        if (binding.exerciseLayout.exerciseLayout.visibility == View.GONE) {
            binding.exerciseLayout.exerciseLayout.visibility = View.VISIBLE
        }
        binding.exerciseLayout.progressbarExercise.progress = exerciseProgress
        binding.exerciseLayout.progressbarExercise.max =
            exercisesList!![currentExercisePosition].getPractisingTime()
        binding.exerciseLayout.tvExerciseName.text =
            exercisesList!![currentExercisePosition].getName()
        binding.exerciseLayout.ivExerciseImage.setImageResource(exercisesList!![currentExercisePosition].getImage())

        if(pauseOffset==0L){
            speakOut("${exercisesList!![currentExercisePosition].getName()} exercise")
        }


        val exercisePractiseTime = exercisesList!![currentExercisePosition].getPractisingTime()

        exerciseCountDownTimer =
            object : CountDownTimer((((exercisePractiseTime + 1) * 1000).toLong() - pauseOffsetL), 1000) {
                override fun onTick(millisUntilFinish: Long) {
                    pauseOffset = ((exercisePractiseTime + 1) * 1000) - millisUntilFinish
                    exerciseProgress++
                    binding.exerciseLayout.progressbarExercise.progress =
                        (exercisePractiseTime + 1) - exerciseProgress
                    binding.exerciseLayout.tvExerciseTimer.text =
                        ((exercisePractiseTime + 1) - exerciseProgress).toString()
                }

                @SuppressLint("NotifyDataSetChanged")
                override fun onFinish() {
                    exercisesList!![currentExercisePosition].setIsSelected(false)
                    exercisesList!![currentExercisePosition].setIsCompleted(true)
                    //? Will call onBindViewHolder again with the new updated data
                    exerciseAdapter.notifyDataSetChanged()
                    //? Hide the exercise layout
                    binding.exerciseLayout.exerciseLayout.visibility = View.GONE
                    if (currentExercisePosition < exercisesList!!.size - 1) {
                        setUpRestCountDownTimer()
                    } else {
                        val intent = Intent(this@ExerciseActivity, FinishActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    pauseOffset=0
                }
            }.start()

    }

    private fun pauseCountDownTimer(){
        if(exerciseCountDownTimer!=null){
            exerciseCountDownTimer!!.cancel()
        }
    }

    override fun onDestroy() {
        if (tts.isSpeaking) {
            tts.stop()
            tts.shutdown()
        }

        if (mediaPlayer.isPlaying) {
            mediaPlayer.stop()
        }

        super.onDestroy()
        if (restCountDownTimer != null) {
            restCountDownTimer?.cancel()
            restProgress = 0
        }

        if (exerciseCountDownTimer != null) {
            exerciseCountDownTimer?.cancel()
            exerciseProgress = 0
        }
    }


}