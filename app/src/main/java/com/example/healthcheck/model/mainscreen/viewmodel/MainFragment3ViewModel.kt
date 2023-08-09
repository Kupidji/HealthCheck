package com.example.healthcheck.model.mainscreen.viewmodel

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthcheck.model.Repositories
import com.example.healthcheck.util.AppDispatchers
import com.example.healthcheck.util.Constants
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.TimeZone

class MainFragment3ViewModel(application: Application) : AndroidViewModel(application) {

    var settings : SharedPreferences
    var settingsForCardio : SharedPreferences

    private val _totalStepsForMonth = MutableSharedFlow<Int>(1, 0, BufferOverflow.DROP_OLDEST)
    val totalStepsForMonth = _totalStepsForMonth.asSharedFlow()

    private val _averageSleepMonth = MutableSharedFlow<String>(1, 0, BufferOverflow.DROP_OLDEST)
    val averageSleepMonth = _averageSleepMonth.asSharedFlow()

    private val _totalWeightForMonth = MutableSharedFlow<Float>(1, 0, BufferOverflow.DROP_OLDEST)
    val totalWeightForMonth = _totalWeightForMonth.asSharedFlow()

    init {
        settings = application.applicationContext.getSharedPreferences(Constants.STEPS, Context.MODE_PRIVATE)
        settingsForCardio = application.applicationContext.getSharedPreferences(Constants.CARDIO, Context.MODE_PRIVATE)
        viewModelScope.launch {
            _totalStepsForMonth.emit(getStepsFromDataForMonth())
        }
        viewModelScope.launch {
            _averageSleepMonth.emit(getSleepFromDataForMonthAverage())
        }
        viewModelScope.launch {
            _totalWeightForMonth.emit(getWeightFromDataForMonth())
        }
    }
    suspend fun getStepsFromDataForMonth() : Int {
        val result = viewModelScope.async(AppDispatchers.default) {
            var list = withContext(AppDispatchers.io) {
                var listOfSteps = Repositories.stepsRepository.getStepsForMonth()
                return@withContext listOfSteps
            }
            var sum = 0

            for (steps in list) {
                sum += steps
            }

            if (list.size != 0){
                sum /= list.size
            }

            return@async sum
        }

        return result.await()
    }

    suspend fun getSleepFromDataForMonthAverage() : String {
        var result = viewModelScope.async(AppDispatchers.default) {
            var list = withContext(AppDispatchers.io) {
                var listOfSleepTime = Repositories.sleepRepository.getTimeOfSleepForMonth()
                return@withContext listOfSleepTime
            }
            var sum = 0L
            var GMT = getGMT()
            var listGMT = GMT.split(":")
            for (sleep in list) {
                sum += sleep.timeOfSleep
            }
            if (list.size != 0){
                sum /= list.size
            }
            var string = SimpleDateFormat("HH:mm").format(sum).toString()
            var listt = string.split(":")
            string = (listt[0].toInt()-listGMT[0].toInt()).toString() + ":" + listt[1]

            return@async string
        }

        return result.await()
    }

    suspend fun getWeightFromDataForMonth() : Float {
        var result = viewModelScope.async(AppDispatchers.default) {
            var list = withContext(AppDispatchers.io) {
                var listOfWeight = Repositories.weightRepository.getWeightForMonth()
                return@withContext listOfWeight
            }
            var sum = 0F

            for (steps in list) {
                sum += steps
            }

            if (list.isNotEmpty()){
                sum /= list.size
            }

            return@async sum
        }

        return  result.await()
    }

    private fun getGMT() : String {
        val tz = TimeZone.getDefault()
        val gmt1 = TimeZone.getTimeZone(tz.id).getDisplayName(false, TimeZone.SHORT)
        if (gmt1.length > 3){
            return gmt1.slice(4..8)
        }
        else{
            return "00:00"
        }

    }

}