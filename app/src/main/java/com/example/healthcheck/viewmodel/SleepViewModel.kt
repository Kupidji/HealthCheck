package com.example.healthcheck.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthcheck.model.Repositories
import com.example.healthcheck.model.sleep.entities.Sleep
import com.example.healthcheck.model.steps.entities.Steps
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.concurrent.ThreadPoolExecutor

class SleepViewModel : ViewModel() {

    suspend fun getSleepFromDataForWeek(sheduler : ThreadPoolExecutor) : String = coroutineScope {
        withContext(sheduler.asCoroutineDispatcher()) {
            var result = async {
                var list = Repositories.sleepRepository.getTimeOfSleepForWeek()
                var sum = "00:00"
                for (sleep in list) {
                var listt = sum.split(":")
                    var firstPart = listt[0].toInt()
                    var secondPart = listt[1].toInt()
                    var string = SimpleDateFormat("HH:mm").format(sleep.timeOfSleep)
                    var stringg = string.split(":")
                    secondPart += stringg[1].toInt()
                    firstPart = firstPart + stringg[0].toInt() + (secondPart / 60)
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
                    var listt = sum.split(":")
                    var firstPart = listt[0].toInt()
                    var secondPart = listt[1].toInt()
                    var string = SimpleDateFormat("HH:mm").format(sleep.timeOfSleep).toString()
                    var stringg = string.split(":")
                    secondPart += stringg[1].toInt()
                    firstPart = firstPart + stringg[0].toInt() + (secondPart / 60)
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
                string = (listt[0].toInt()).toString() + ":" + listt[1]
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
                for (sleep in list) {
                    sum += sleep.timeOfSleep
                }
                if (list.size != 0){
                    sum /= list.size
                }
                var string = SimpleDateFormat("HH:mm").format(sum).toString()
                var listt = string.split(":")
                string = (listt[0].toInt()).toString() + ":" + listt[1]
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

}