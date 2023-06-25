package com.example.healthcheck.viewmodel

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthcheck.model.Repositories
import com.example.healthcheck.model.sleep.entities.Sleep
import com.example.healthcheck.util.Constants
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.TimeZone
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

class SleepViewModel(application: Application) : AndroidViewModel(application) {

    lateinit var settings : SharedPreferences

    var lastId = MutableLiveData<Int?>()
    var lastDate = MutableLiveData<Long?>()
    var sleepForWeek = MutableLiveData<String?>()
    var sleepForMonth = MutableLiveData<String?>()
    var averageSleepForMonth = MutableLiveData<String?>()
    var averageSleepForWeek = MutableLiveData<String?>()

    private val tripletsPool = ThreadPoolExecutor(3, 3, 5L, TimeUnit.SECONDS, LinkedBlockingQueue())

    init {
        settings = application.applicationContext.getSharedPreferences(Constants.SLEEP, Context.MODE_PRIVATE)

        setCurrentId()
        setCurrentDate()
        setCurrentSleepMonth()
        setCurrentAverageSleepMonth()
        setCurrentSleepWeek()
        setCurrentAverageSleepWeek()
    }

    suspend fun getSleepFromDataForWeek(sheduler : ThreadPoolExecutor) : String = coroutineScope {
        withContext(sheduler.asCoroutineDispatcher()) {
            var result = async {
                var list = Repositories.sleepRepository.getTimeOfSleepForWeek()
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
            }
            result.await()
        }
    }

    suspend fun getSleepFromDataForMonth(sheduler : ThreadPoolExecutor) : String = coroutineScope {
        withContext(sheduler.asCoroutineDispatcher()) {
            var result = async {
                var list = Repositories.sleepRepository.getTimeOfSleepForMonth()
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
            }
            result.await()
        }
    }

    suspend fun getLastIdFromData(sheduler : ThreadPoolExecutor) : Int = coroutineScope {
        withContext(sheduler.asCoroutineDispatcher()) {
            var result = async {
                var list = Repositories.sleepRepository.getTimeOfSleepForDay()
                var lastId = 0
                for (sleep in list) {
                    lastId = sleep.id
                }
                return@async lastId
            }
            result.await()
        }
    }

    suspend fun getLastDateFromData(sheduler : ThreadPoolExecutor) : Long = coroutineScope {
        withContext(sheduler.asCoroutineDispatcher()) {
            var result = async {
                var list = Repositories.sleepRepository.getTimeOfSleepForDay()
                var lastDate = 0L
                for (sleep in list) {
                    lastDate = sleep.date
                }
                return@async lastDate
            }
            result.await()
        }
    }

    suspend fun getSleepFromDataForWeekAverage(sheduler : ThreadPoolExecutor) : String = coroutineScope {
        withContext(sheduler.asCoroutineDispatcher()) {
            var result = async {
                var list = Repositories.sleepRepository.getTimeOfSleepForWeek()
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
            }
            result.await()
        }
    }

    suspend fun getSleepFromDataForMonthAverage(sheduler : ThreadPoolExecutor) : String = coroutineScope {
        withContext(sheduler.asCoroutineDispatcher()) {
            var result = async {
                var list = Repositories.sleepRepository.getTimeOfSleepForMonth()
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
            }
            result.await()
        }
    }

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
        viewModelScope.launch(Dispatchers.Main, CoroutineStart.DEFAULT) {
            lastId.value = getLastIdFromData(tripletsPool)
        }
    }

    fun setCurrentDate() {
        viewModelScope.launch(Dispatchers.Main, CoroutineStart.DEFAULT) {
            lastDate.value = getLastDateFromData(tripletsPool)
        }
    }

    fun setCurrentSleepWeek() {
        viewModelScope.launch(Dispatchers.Main, CoroutineStart.DEFAULT) {
            sleepForWeek.value = getSleepFromDataForWeek(tripletsPool)
        }
    }

    fun setCurrentSleepMonth() {
        viewModelScope.launch(Dispatchers.Main, CoroutineStart.DEFAULT) {
            sleepForMonth.value = getSleepFromDataForMonth(tripletsPool)
        }
    }

    fun setCurrentAverageSleepMonth() {
        viewModelScope.launch(Dispatchers.Main, CoroutineStart.DEFAULT) {
            averageSleepForMonth.value = getSleepFromDataForMonthAverage(tripletsPool)
        }
    }

    fun setCurrentAverageSleepWeek() {
        viewModelScope.launch(Dispatchers.Main, CoroutineStart.DEFAULT) {
            averageSleepForWeek.value = getSleepFromDataForWeekAverage(tripletsPool)
        }
    }

}