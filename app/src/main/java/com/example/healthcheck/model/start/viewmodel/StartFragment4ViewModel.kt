package com.example.healthcheck.model.start.viewmodel

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData

class StartFragment4ViewModel(application: Application) : AndroidViewModel(application) {

    lateinit var settings : SharedPreferences

    var fio = MutableLiveData<String?>()
    var gender = MutableLiveData<Boolean?>()

    init {
        settings = application.applicationContext.getSharedPreferences("start_info", Context.MODE_PRIVATE)
    }

}