package com.example.healthcheck.viewmodel

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthcheck.model.Repositories
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

class MainFragment3ViewModel(application: Application) : AndroidViewModel(application) {

    lateinit var settings : SharedPreferences

    var totalStepsForMonth = MutableLiveData<Int?>()
    var averageSleepMonth = MutableLiveData<String?>()

    init {
        settings = application.applicationContext.getSharedPreferences("targetPref", Context.MODE_PRIVATE)
        val tripletsPool = ThreadPoolExecutor(3, 3, 5L, TimeUnit.SECONDS, LinkedBlockingQueue())
        viewModelScope.launch(Dispatchers.Main, CoroutineStart.DEFAULT) {
            totalStepsForMonth.value = getStepsFromDataForMonth(tripletsPool)
        }
        viewModelScope.launch(Dispatchers.Main, CoroutineStart.DEFAULT) {
            averageSleepMonth.value = getSleepFromDataForMonthAverage(tripletsPool)
        }
    }
    suspend fun getStepsFromDataForMonth(sheduler : ThreadPoolExecutor) : Int = coroutineScope {
        withContext(sheduler.asCoroutineDispatcher()) {
            var result = async {
                var list = Repositories.stepsRepository.getStepsForMonth()
                var sum = 0
                for (steps in list) {
                    sum += steps
                }
                return@async sum / 30
            }

            result.await()
        }
    }

    suspend fun getSleepFromDataForMonthAverage(sheduler : ThreadPoolExecutor) : String = coroutineScope {
        withContext(sheduler.asCoroutineDispatcher()) {
            var result = async {
                var list = Repositories.sleepRepository.getTimeOfSleepForMonth()
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

}