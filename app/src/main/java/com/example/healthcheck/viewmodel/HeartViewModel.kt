package com.example.healthcheck.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthcheck.model.Repositories
import com.example.healthcheck.model.heart.entities.Heart
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

class HeartViewModel : ViewModel() {


    suspend fun getCardioFromDataUpPressure(sheduler : ThreadPoolExecutor): Int = coroutineScope {
        withContext(sheduler.asCoroutineDispatcher()) {
            var result = async {
                var list = Repositories.heartRepository.getCardioForDay()
                var sum = 0
                for (heart in list) {
                    sum = heart.pressureUp
                }
                return@async sum
            }
            result.await()
        }
    }

    suspend fun getCardioFromDataDownPressure(sheduler : ThreadPoolExecutor): Int = coroutineScope {
        withContext(sheduler.asCoroutineDispatcher()) {
            var result = async {
                var list = Repositories.heartRepository.getCardioForDay()
                var sum = 0
                for (heart in list) {
                    sum = heart.pressureDown
                }
                return@async sum
            }
            result.await()
        }
    }

    suspend fun getCardioFromDataPulse(sheduler : ThreadPoolExecutor): Int = coroutineScope {
        withContext(sheduler.asCoroutineDispatcher()) {
            var result = async {
                var list = Repositories.heartRepository.getCardioForDay()
                var sum = 0
                for (heart in list) {
                    sum = heart.pulse
                }
                return@async sum
            }
            result.await()
        }
    }

    fun insertHeart(heart : Heart) {
        viewModelScope.launch {
            Repositories.heartRepository.insertCardio(heart)
        }
    }

}