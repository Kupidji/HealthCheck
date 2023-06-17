package com.example.healthcheck.viewmodel

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel

class MainFragment1ViewModel(application: Application) : AndroidViewModel(application) {

    lateinit var settings : SharedPreferences
    lateinit var settingsForSleep : SharedPreferences
    lateinit var settingsForWeight : SharedPreferences

    init {
        settings = application.applicationContext.getSharedPreferences("targetPref", Context.MODE_PRIVATE)
        settingsForSleep = application.applicationContext.getSharedPreferences("sleep", Context.MODE_PRIVATE)
        settingsForWeight = application.applicationContext.getSharedPreferences("weight", Context.MODE_PRIVATE)
    }

}