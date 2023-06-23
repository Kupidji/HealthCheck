package com.example.healthcheck.viewmodel

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
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
import java.text.SimpleDateFormat
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

class MainFragment2ViewModel(application: Application) : AndroidViewModel(application) {

    lateinit var settings : SharedPreferences

    var totalStepsForWeek = MutableLiveData<Int?>()
    var averageSleep = MutableLiveData<String?>()
    var totalWeightForWeight = MutableLiveData<Float?>()

    init {
        settings = application.applicationContext.getSharedPreferences("targetPref", Context.MODE_PRIVATE)
        val tripletsPool = ThreadPoolExecutor(3, 3, 5L, TimeUnit.SECONDS, LinkedBlockingQueue())
        viewModelScope.launch(Dispatchers.Main, CoroutineStart.DEFAULT) {
            totalStepsForWeek.value = getStepsFromDataForWeek(tripletsPool)
        }
        viewModelScope.launch(Dispatchers.Main, CoroutineStart.DEFAULT) {
            averageSleep.value = getSleepFromDataForWeekAverage(tripletsPool)
        }
        viewModelScope.launch(Dispatchers.Main, CoroutineStart.DEFAULT) {
            totalWeightForWeight.value = getWeightFromDataForWeek(tripletsPool)
        }
    }
    suspend fun getStepsFromDataForWeek(sheduler : ThreadPoolExecutor) : Int = coroutineScope {
        withContext(sheduler.asCoroutineDispatcher()) {
            var result = async {
                var list = Repositories.stepsRepository.getStepsForWeek()
                var sum = 0
                for (steps in list) {
                    sum += steps
                }
                if (list.size != 0) {
                    sum /= list.size
                }
                return@async sum
            }

            result.await()
        }
    }

    suspend fun getSleepFromDataForWeekAverage(sheduler : ThreadPoolExecutor) : String = coroutineScope {
        withContext(sheduler.asCoroutineDispatcher()) {
            var result = async {
                var list = Repositories.sleepRepository.getTimeOfSleepForWeek()
                var sum = 0L
                for (sleep in list) {
                    sum += sleep.timeOfSleep
                }
                if (list.size != 0){
                    sum /= list.size
                }
                var string = SimpleDateFormat("HH:mm").format(sum).toString()
                var listt = string.split(":")
                string = (listt[0].toInt()-5).toString() + ":" + listt[1]
                return@async string
            }
            result.await()
        }
    }

    suspend fun getWeightFromDataForWeek(sheduler : ThreadPoolExecutor) : Float = coroutineScope {
        withContext(sheduler.asCoroutineDispatcher()) {
            var result = async {
                var list = Repositories.weightRepository.getWeightForWeek()
                var sum = 0F
                for (steps in list) {
                    sum += steps
                }
                if (list.isNotEmpty()){
                    sum /= list.size
                }
                return@async sum
            }
            result.await()
        }
    }

}