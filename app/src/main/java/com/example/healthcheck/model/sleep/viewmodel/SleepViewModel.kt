package com.example.healthcheck.viewmodel

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthcheck.model.Repositories
import com.example.healthcheck.model.sleep.entities.Sleep
import com.example.healthcheck.util.AppDispatchers
import com.example.healthcheck.util.Constants
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.TimeZone

class SleepViewModel(application: Application) : AndroidViewModel(application) {

    var settings : SharedPreferences

    private val _lastId = MutableSharedFlow<Int>()
    val lastId = _lastId.asSharedFlow()

    private val _lastDate = MutableSharedFlow<Long>()
    val lastDate = _lastDate.asSharedFlow()

    private val _sleepForWeek = MutableSharedFlow<String>()
    val sleepForWeek = _sleepForWeek.asSharedFlow()

    private val _sleepForMonth = MutableSharedFlow<String>()
    val sleepForMonth = _sleepForMonth.asSharedFlow()

    private val _averageSleepForWeek = MutableSharedFlow<String>()
    val averageSleepForWeek = _averageSleepForWeek.asSharedFlow()

    private val _averageSleepForMonth = MutableSharedFlow<String>()
    val averageSleepForMonth = _averageSleepForMonth.asSharedFlow()

    init {
        settings = application.applicationContext.getSharedPreferences(Constants.SLEEP, Context.MODE_PRIVATE)

        setCurrentId()
        setCurrentDate()
        setCurrentSleepMonth()
        setCurrentAverageSleepMonth()
        setCurrentSleepWeek()
        setCurrentAverageSleepWeek()
    }

    suspend fun getSleepFromDataForWeek() : String = viewModelScope.async(AppDispatchers.default) {
        val list = withContext(AppDispatchers.io) {
            return@withContext Repositories.sleepRepository.getTimeOfSleepForWeek()
        }
        var sum = "00:00"
        for (sleep in list) {
            var GMT = getGMT()
            var listGMT = GMT.split(":")
            var listt = sum.split(":")
            var firstPart = listt[0].toInt()
            var secondPart = listt[1].toInt()
            var string = SimpleDateFormat("HH:mm").format(sleep.timeOfSleep)
            var stringg = string.split(":")
            secondPart += stringg[1].toInt()
            firstPart = firstPart + stringg[0].toInt() + (secondPart / 60) - listGMT[0].toInt()
            secondPart %= 60
            if (secondPart.toString().length < 2){
                sum = firstPart.toString() + ":0" + secondPart.toString()
            }
            else{
                sum = firstPart.toString() + ":" + secondPart.toString()
            }
        }
        return@async sum
    }.await()

    suspend fun getSleepFromDataForMonth() : String = viewModelScope.async(AppDispatchers.default) {
        val list = withContext(AppDispatchers.io) {
            return@withContext Repositories.sleepRepository.getTimeOfSleepForMonth()
        }
        var sum = "00:00"
        for (sleep in list) {
            var GMT = getGMT()
            var listGMT = GMT.split(":")
            var listt = sum.split(":")
            var firstPart = listt[0].toInt()
            var secondPart = listt[1].toInt()
            var string = SimpleDateFormat("HH:mm").format(sleep.timeOfSleep).toString()
            var stringg = string.split(":")
            secondPart += stringg[1].toInt()
            firstPart = firstPart + stringg[0].toInt() + (secondPart / 60) - listGMT[0].toInt()
            secondPart %= 60
            if (secondPart.toString().length < 2){
                sum = firstPart.toString() + ":0" + secondPart.toString()
            }
            else{
                sum = firstPart.toString() + ":" + secondPart.toString()
            }
        }
        return@async sum
    }.await()

    suspend fun getLastIdFromData() : Int = viewModelScope.async(AppDispatchers.default) {
        val list = withContext(AppDispatchers.io) {
            return@withContext Repositories.sleepRepository.getTimeOfSleepForDay()
        }
        var lastId = 0
        for (sleep in list) {
            lastId = sleep.id
        }
        return@async lastId
    }.await()

    suspend fun getLastDateFromData() : Long = viewModelScope.async(AppDispatchers.default) {
        val list = withContext(AppDispatchers.io) {
            return@withContext Repositories.sleepRepository.getTimeOfSleepForDay()
        }
        var lastDate = 0L
        for (sleep in list) {
            lastDate = sleep.date
        }
        return@async lastDate
    }.await()

    suspend fun getSleepFromDataForWeekAverage() : String = viewModelScope.async(AppDispatchers.default) {
        val list = withContext(AppDispatchers.io) {
            return@withContext Repositories.sleepRepository.getTimeOfSleepForWeek()
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
        string = (listt[0].toInt() - listGMT[0].toInt()).toString() + ":" + listt[1]
        return@async string
    }.await()

    suspend fun getSleepFromDataForMonthAverage() : String = viewModelScope.async(AppDispatchers.default) {
        val list = withContext(AppDispatchers.io) {
            return@withContext Repositories.sleepRepository.getTimeOfSleepForMonth()
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
        string = (listt[0].toInt() - listGMT[0].toInt()).toString() + ":" + listt[1]
        return@async string
    }.await()

    fun insertSleep(sleep : Sleep) {
        viewModelScope.launch {
            Repositories.sleepRepository.insertTimeOfSleep(sleep)
        }
    }

    fun updateSleep(sleep: Sleep) {
        viewModelScope.launch {
            Repositories.sleepRepository.updateTimeOfSleep(sleep)
        }
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

    fun setCurrentId() {
        viewModelScope.launch {
            _lastId.emit(getLastIdFromData())
        }
    }

    fun setCurrentDate() {
        viewModelScope.launch {
            _lastDate.emit(getLastDateFromData())
        }
    }

    fun setCurrentSleepWeek() {
        viewModelScope.launch {
            _sleepForWeek.emit(getSleepFromDataForWeek())
        }
    }

    fun setCurrentSleepMonth() {
        viewModelScope.launch {
            _sleepForMonth.emit(getSleepFromDataForMonth())
        }
    }

    fun setCurrentAverageSleepMonth() {
        viewModelScope.launch {
            _averageSleepForMonth.emit(getSleepFromDataForMonthAverage())
        }
    }

    fun setCurrentAverageSleepWeek() {
        viewModelScope.launch {
            _averageSleepForWeek.emit(getSleepFromDataForWeekAverage())
        }
    }

}