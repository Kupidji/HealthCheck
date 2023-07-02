package com.example.healthcheck.model.mainscreen.viewmodel

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.healthcheck.model.Repositories
import com.example.healthcheck.util.Constants
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

class MainFragment1ViewModel(application: Application) : AndroidViewModel(application) {

    lateinit var settings : SharedPreferences
    lateinit var settingsForSleep : SharedPreferences
    lateinit var settingsForWeight : SharedPreferences
    lateinit var settingsForCardio : SharedPreferences

    var daySteps = MutableLiveData<Long?>()
    var daySleep = MutableLiveData<Long?>()

    var currentDate = Calendar.getInstance().timeInMillis
    private var tripletsPool = ThreadPoolExecutor(3, 3, 5L, TimeUnit.SECONDS, LinkedBlockingQueue())
    init {
        settings = application.applicationContext.getSharedPreferences(Constants.STEPS, Context.MODE_PRIVATE)
        settingsForSleep = application.applicationContext.getSharedPreferences(Constants.SLEEP, Context.MODE_PRIVATE)
        settingsForWeight = application.applicationContext.getSharedPreferences(Constants.WEIGHT, Context.MODE_PRIVATE)
        settingsForCardio = application.applicationContext.getSharedPreferences(Constants.CARDIO, Context.MODE_PRIVATE)

        viewModelScope.launch(Dispatchers.Main, CoroutineStart.DEFAULT) {
            daySteps.value = getLastDateFromDataSteps(tripletsPool)
        }
        viewModelScope.launch(Dispatchers.Main, CoroutineStart.DEFAULT) {
            daySleep.value = getLastDateFromDataSleep(tripletsPool)
        }
    }

    suspend fun getLastDateFromDataSteps(sheduler : ThreadPoolExecutor) : Long = coroutineScope {
        withContext(sheduler.asCoroutineDispatcher()) {
            var result = async {
                var date = currentDate
                if (Repositories.stepsRepository.getLastDate() != null) {
                    date = Repositories.stepsRepository.getLastDate().date
                }
                return@async date
            }
            result.await()
        }
    }

    suspend fun getLastDateFromDataSleep(sheduler : ThreadPoolExecutor) : Long = coroutineScope {
        withContext(sheduler.asCoroutineDispatcher()) {
            var result = async {
                var list = Repositories.sleepRepository.getTimeOfSleepForDay()
                var lastDate = currentDate
                for (sleep in list) {
                    lastDate = sleep.date
                }
                return@async lastDate
            }
            result.await()
        }
    }

}