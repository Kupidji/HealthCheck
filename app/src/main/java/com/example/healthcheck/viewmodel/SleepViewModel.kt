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

//    suspend fun getSleepFromDataForWeek(sheduler : ThreadPoolExecutor) : String = coroutineScope {
//        withContext(sheduler.asCoroutineDispatcher()) {
//            var result = async {
//                var list = Repositories.sleepRepository.getTimeOfSleepForWeek()
//                var sum = "00:00"
//                var firstPart = 0
//                var secondPart = 0
//                for (sleep in list) {
//                    var string = SimpleDateFormat("HH:mm").format(sleep.timeOfSleep)
//                    firstPart = sum.substring(1..2).toInt() + string.substring(1..2).toInt()
//                    secondPart = sum.substring(4..5).toInt() + string.substring(4..5).toInt()
//                    sum = "." + firstPart.toString() + ":" + secondPart.toString()
//                }
//                return@async sum
//            }
//
//            result.await()
//        }
//    }

}