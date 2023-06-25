package com.example.healthcheck.viewmodel

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.healthcheck.util.Constants

class StartFragment5ViewModel(application: Application) : AndroidViewModel(application) {

    lateinit var settings : SharedPreferences

    var age = MutableLiveData<Int?>()
    var weightStart = MutableLiveData<Float?>()

    init {
        settings = application.applicationContext.getSharedPreferences(Constants.START, Context.MODE_PRIVATE)
    }
}