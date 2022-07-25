package com.kerollosragaie.workoutboost.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import com.kerollosragaie.workoutboost.databinding.ActivityFinishBinding
import java.util.*

class FinishActivity : AppCompatActivity(), TextToSpeech.OnInitListener  {
    private lateinit var binding:ActivityFinishBinding

    //*** Text to speech
    private lateinit var tts: TextToSpeech

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFinishBinding.inflate(layoutInflater)
        setContentView(binding.root)

        settingUpToolBar()

        settingUpTTS()

        binding.btnFinish.setOnClickListener {
            finish()
        }
    }

    //*** For app toolbar
    private fun settingUpToolBar() {
        setSupportActionBar(binding.toolbarFinish)
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
        speakOut("Congratulations! You have completed the workout.")
    }

    override fun onDestroy() {
        super.onDestroy()
        if (tts.isSpeaking) {
            tts.stop()
            tts.shutdown()
        }
    }

}