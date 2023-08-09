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
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.TimeZone

class MainFragment2ViewModel(application: Application) : AndroidViewModel(application) {

    var settings : SharedPreferences
    var settingsForCardio : SharedPreferences

    private val _totalStepsForWeek = MutableSharedFlow<Int>()
    val totalStepsForWeek = _totalStepsForWeek.asSharedFlow()

    private val _averageSleep = MutableSharedFlow<String>()
    val averageSleep = _averageSleep.asSharedFlow()

    private val _totalWeightForWeight = MutableSharedFlow<Float>()
    val totalWeightForWeight = _totalWeightForWeight.asSharedFlow()

    init {
        settings = application.applicationContext.getSharedPreferences(Constants.STEPS, Context.MODE_PRIVATE)
        settingsForCardio = application.applicationContext.getSharedPreferences(Constants.CARDIO, Context.MODE_PRIVATE)

        viewModelScope.launch {
            _totalStepsForWeek.emit(getStepsFromDataForWeek())
        }
        viewModelScope.launch {
            _averageSleep.emit(getSleepFromDataForWeekAverage())
        }
        viewModelScope.launch {
            _totalWeightForWeight.emit(getWeightFromDataForWeek())
        }

    }

    suspend fun getStepsFromDataForWeek() : Int {
        var result = viewModelScope.async(AppDispatchers.io) {
            var list = Repositories.stepsRepository.getStepsForWeek()

            var sumResult = withContext(AppDispatchers.default) {
                var sum = 0

                for (steps in list) {
                    sum += steps
                }

                if (list.size != 0) {
                    sum /= list.size
                }
                return@withContext sum
            }

            return@async sumResult
        }

        return result.await()
    }

    suspend fun getSleepFromDataForWeekAverage() : String {
        var result = viewModelScope.async(AppDispatchers.io) {
            var list = Repositories.sleepRepository.getTimeOfSleepForWeek()

            var stringResult = withContext(AppDispatchers.default) {
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
                return@withContext string
            }

            return@async stringResult
        }

        return result.await()
    }

    suspend fun getWeightFromDataForWeek() : Float {
        var result = viewModelScope.async(AppDispatchers.io) {
            var list = Repositories.weightRepository.getWeightForWeek()

            var sumResult = withContext(AppDispatchers.default) {
                var sum = 0F

                for (steps in list) {
                    sum += steps
                }

                if (list.isNotEmpty()){
                    sum /= list.size
                }
                return@withContext sum
            }
            return@async sumResult
        }
        return result.await()
    }

    fun getGMT() : String {
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