package com.example.healthcheck.viewmodel

import androidx.lifecycle.ViewModel
import com.example.healthcheck.model.Repositories
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.concurrent.ThreadPoolExecutor

class SleepViewModel : ViewModel() {

//переписать

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
                    firstPart = firstPart + stringg[0].toInt() - 5
                    secondPart = secondPart + stringg[1].toInt()
                    sum = firstPart.toString() + ":" + secondPart.toString()
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
                    firstPart = firstPart + stringg[0].toInt() - 5
                    secondPart = secondPart + stringg[1].toInt()
                    sum = firstPart.toString() + ":" + secondPart.toString()
                }
                return@async sum
            }
            result.await()
        }
    }

}