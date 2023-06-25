package com.example.healthcheck.viewmodel

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.healthcheck.util.Constants

class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    lateinit var settings : SharedPreferences

    var fio = MutableLiveData<String?>()
    var age = MutableLiveData<String?>()
    var weightStart = MutableLiveData<String?>()
    var heightStart = MutableLiveData<String?>()

    init {
        settings = application.applicationContext.getSharedPreferences(Constants.START, Context.MODE_PRIVATE)
    }
}