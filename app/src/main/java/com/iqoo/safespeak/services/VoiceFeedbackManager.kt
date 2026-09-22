package com.iqoo.safespeak.services

import android.content.Context
import android.speech.tts.TextToSpeech
import android.util.Log
import java.util.Locale

class VoiceFeedbackManager(context: Context) : TextToSpeech.OnInitListener {

    private var tts: TextToSpeech? = TextToSpeech(context.applicationContext, this)
    private var isInitialized = false

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts?.setLanguage(Locale.US)
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("VoiceFeedbackManager", "TTS Language US not supported offline")
            } else {
                isInitialized = true
            }
        } else {
            Log.e("VoiceFeedbackManager", "TTS Initialization failed")
        }
    }

    fun speak(text: String, isDiscreetMode: Boolean = false) {
        if (isDiscreetMode) return // Silent mode active
        if (isInitialized && tts != null) {
            tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "SafeSpeak_Audio_Utterance")
        }
    }

    fun shutdown() {
        try {
            tts?.stop()
            tts?.shutdown()
            tts = null
        } catch (e: Exception) {
            Log.e("VoiceFeedbackManager", "Error shutting down TTS", e)
        }
    }
}
