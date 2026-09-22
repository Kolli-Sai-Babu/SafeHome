package com.iqoo.safespeak

import android.app.Application
import com.iqoo.safespeak.data.local.AppDatabase
import com.iqoo.safespeak.data.repository.SafeSpeakRepository

class SafeSpeakApplication : Application() {

    val database by lazy { AppDatabase.getDatabase(this) }
    val repository by lazy { SafeSpeakRepository(database.emergencyDao()) }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        lateinit var instance: SafeSpeakApplication
            private set
    }
}
