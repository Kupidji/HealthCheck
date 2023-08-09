package com.example.healthcheck.model.mainscreen.viewmodel

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.healthcheck.model.Repositories
import com.example.healthcheck.util.AppDispatchers
import com.example.healthcheck.util.Constants
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

class MainFragment1ViewModel(application: Application) : AndroidViewModel(application) {

    var settings : SharedPreferences
    var settingsForSleep : SharedPreferences
    var settingsForWeight : SharedPreferences
    var settingsForCardio : SharedPreferences

    private val _daySteps = MutableSharedFlow<Long>()
    val daySteps = _daySteps.asSharedFlow()

    private val _daySleep = MutableSharedFlow<Long>()
    val daySleep = _daySleep.asSharedFlow()

    private val _dayWeight = MutableSharedFlow<Float>(1, 0, BufferOverflow.DROP_OLDEST)
    val dayWeight = _dayWeight.asSharedFlow()

    var currentDate = Calendar.getInstance().timeInMillis

    init {
        settings = application.applicationContext.getSharedPreferences(Constants.STEPS, Context.MODE_PRIVATE)
        settingsForSleep = application.applicationContext.getSharedPreferences(Constants.SLEEP, Context.MODE_PRIVATE)
        settingsForWeight = application.applicationContext.getSharedPreferences(Constants.WEIGHT, Context.MODE_PRIVATE)
        settingsForCardio = application.applicationContext.getSharedPreferences(Constants.CARDIO, Context.MODE_PRIVATE)

        viewModelScope.launch {
            _daySteps.emit(getLastDateFromDataSteps())
        }
        viewModelScope.launch {
            _daySleep.emit(getLastDateFromDataSleep())
        }
        viewModelScope.launch {
            _dayWeight.emit(settingsForWeight.getFloat(Constants.WEIGHT_FOR_DAY, 0F))
        }
    }

    suspend fun getLastDateFromDataSteps() : Long {
        val result = viewModelScope.async(AppDispatchers.io) {
            var date = currentDate
            if (Repositories.stepsRepository.getLastDate() != null) {
                date = Repositories.stepsRepository.getLastDate().date
            }
            return@async date
        }

        return result.await()
    }

    suspend fun getLastDateFromDataSleep() : Long {
        val result = viewModelScope.async(AppDispatchers.io) {
            var list = Repositories.sleepRepository.getTimeOfSleepForDay()
            var lastDate = currentDate
            for (sleep in list) {
                lastDate = sleep.date
            }
            return@async lastDate
        }

        return result.await()
    }

}