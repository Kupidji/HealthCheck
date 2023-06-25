package com.example.healthcheck.model.start.viewmodel

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData

class StartFragment5ViewModel(application: Application) : AndroidViewModel(application) {

    lateinit var settings : SharedPreferences

    var age = MutableLiveData<Int?>()
    var weightStart = MutableLiveData<Float?>()

    init {
        settings = application.applicationContext.getSharedPreferences("start_info", Context.MODE_PRIVATE)
    }
}