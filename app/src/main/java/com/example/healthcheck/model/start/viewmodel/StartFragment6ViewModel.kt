package com.example.healthcheck.viewmodel

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.healthcheck.util.Constants

class StartFragment6ViewModel(application: Application) : AndroidViewModel(application) {

    lateinit var settings : SharedPreferences

    var heightStart = MutableLiveData<Float?>()

    init {
        settings = application.applicationContext.getSharedPreferences(Constants.START, Context.MODE_PRIVATE)
    }
}