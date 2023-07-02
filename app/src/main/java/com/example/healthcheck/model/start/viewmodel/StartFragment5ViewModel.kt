package com.example.healthcheck.viewmodel

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthcheck.model.Repositories
import com.example.healthcheck.model.weight.entities.Weight
import com.example.healthcheck.util.Constants
import kotlinx.coroutines.launch

class StartFragment5ViewModel(application: Application) : AndroidViewModel(application) {

    lateinit var settings : SharedPreferences
    lateinit var settingsForWeight : SharedPreferences

    init {
        settings = application.applicationContext.getSharedPreferences(Constants.START, Context.MODE_PRIVATE)
        settingsForWeight = application.applicationContext.getSharedPreferences(Constants.WEIGHT, Context.MODE_PRIVATE)
    }

    fun insertWeight(ourWeight: Weight) {
        viewModelScope.launch {
            Repositories.weightRepository.insertWeight(ourWeight)
        }
    }

}