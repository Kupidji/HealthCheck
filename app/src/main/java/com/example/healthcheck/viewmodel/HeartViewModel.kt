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


    var list = emptyList<Int>()

    init {
        viewModelScope.launch {
            list = getCardioFromData(ThreadPoolExecutor(3, 3, 5L, TimeUnit.SECONDS, LinkedBlockingQueue()))
        }
    }

    suspend fun getCardioFromData(sheduler : ThreadPoolExecutor): List<Int> = coroutineScope {
        withContext(sheduler.asCoroutineDispatcher()) {
            var result = async {
                var list = Repositories.heartRepository.getCardioForDay()
                var sum = mutableListOf<Int>()
                for (heart in list) {
                    sum.add(heart.pressureUp)
                    sum.add(heart.pressureDown)
                    sum.add(heart.pulse)
                }
                if (sum.isEmpty()) {
                    sum = mutableListOf(0, 0, 0)
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