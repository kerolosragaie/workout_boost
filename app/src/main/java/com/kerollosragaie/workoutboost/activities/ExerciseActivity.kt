package com.kerollosragaie.workoutboost.activities

import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import com.kerollosragaie.workoutboost.Constants
import com.kerollosragaie.workoutboost.R
import com.kerollosragaie.workoutboost.databinding.ActivityExerciseBinding
import com.kerollosragaie.workoutboost.models.Exercise
import java.util.*
import kotlin.collections.ArrayList

class ExerciseActivity : AppCompatActivity(), TextToSpeech.OnInitListener {
    private lateinit var binding: ActivityExerciseBinding

    //***For countdown timer
    private var restCountDownTimer: CountDownTimer? = null
    private var restProgress: Int = 0

    //*** For exercise countdown timer
    private var exerciseCountDownTimer: CountDownTimer? = null
    private var exerciseProgress: Int = 0

    //*** Get exercises list
    private var exercisesList: ArrayList<Exercise>? = null
    private var currentExercisePosition = -1

    //*** Text to speech
    private lateinit var tts: TextToSpeech

    //*** Media player
    private lateinit var mediaPlayer: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExerciseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        settingUpToolBar()

        setUpExercises()

        setUpRestCountDownTimer()

        settingUpTTS()

    }

    //*** For app toolbar
    private fun settingUpToolBar() {
        setSupportActionBar(binding.toolbarExercise)
        //*** shows top back button
        if (supportActionBar != null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        binding.toolbarExercise.setNavigationOnClickListener {
            onBackPressed()
        }
        supportActionBar?.setDisplayShowTitleEnabled(false)
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

        //For media player
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
        restCountDownTimer = object : CountDownTimer(11000, 1000) {
            override fun onTick(p0: Long) {
                restProgress++
                binding.restLayout.progressBar.progress = 11 - restProgress
                binding.restLayout.tvTimer.text = (11 - restProgress).toString()
            }

            override fun onFinish() {
                binding.restLayout.restLayout.visibility = View.GONE
                currentExercisePosition++ //Go to next exercise
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
        setExerciseProgressBar()
    }

    private fun setExerciseProgressBar() {
        if (binding.exerciseLayout.exerciseLayout.visibility == View.GONE) {
            binding.exerciseLayout.exerciseLayout.visibility = View.VISIBLE
        }
        binding.exerciseLayout.progressbarExercise.progress = exerciseProgress
        binding.exerciseLayout.progressbarExercise.max =
            exercisesList!![currentExercisePosition].getPractisingTime()
        binding.exerciseLayout.tvExerciseName.text =
            exercisesList!![currentExercisePosition].getName()
        binding.exerciseLayout.ivExerciseImage.setImageResource(exercisesList!![currentExercisePosition].getImage())

        speakOut("${exercisesList!![currentExercisePosition].getName()} exercise")

        val exercisePractiseTime = exercisesList!![currentExercisePosition].getPractisingTime()

        exerciseCountDownTimer =
            object : CountDownTimer(((exercisePractiseTime + 1) * 1000).toLong(), 1000) {
                override fun onTick(p0: Long) {
                    exerciseProgress++
                    binding.exerciseLayout.progressbarExercise.progress =
                        (exercisePractiseTime + 1) - exerciseProgress
                    binding.exerciseLayout.tvExerciseTimer.text =
                        ((exercisePractiseTime + 1) - exerciseProgress).toString()
                }

                override fun onFinish() {
                    binding.exerciseLayout.exerciseLayout.visibility = View.GONE
                    if (currentExercisePosition < exercisesList!!.size - 1) {
                        setUpRestCountDownTimer()
                    } else {
                        //TODO jump to finish screen
                        speakOut("Congratulations! You have completed all the workouts.")
                    }
                }
            }.start()
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