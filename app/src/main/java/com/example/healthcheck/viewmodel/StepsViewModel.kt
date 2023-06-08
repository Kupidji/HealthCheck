package com.example.healthcheck.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthcheck.model.Repositories
import com.example.healthcheck.model.steps.entities.Steps
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.ThreadPoolExecutor

class StepsViewModel : ViewModel() {

    suspend fun getStepsFromDataForWeek(sheduler : ThreadPoolExecutor) : Int = coroutineScope {
        withContext(sheduler.asCoroutineDispatcher()) {
            var result = async {
                var list = Repositories.stepsRepository.getStepsForWeek()
                var sum = 0
                for (steps in list) {
                    sum += steps
                }
                return@async sum
            }

            result.await()
        }
    }

    fun insertSteps(ourSteps : Steps) {
        viewModelScope.launch {
            Repositories.stepsRepository.insertCountOfSteps(ourSteps)
        }
    }


}