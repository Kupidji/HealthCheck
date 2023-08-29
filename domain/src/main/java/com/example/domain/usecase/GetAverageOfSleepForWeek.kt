package com.example.domain.usecase

import com.example.domain.AppDispatchers
import com.example.domain.repository.SleepRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.lang.Math.abs

class GetAverageOfSleepForWeek(private val repository: SleepRepository) {

    suspend fun execute() : Flow<String> = withContext(AppDispatchers.default) {
        val listFlow = withContext(AppDispatchers.io) {
            return@withContext repository.getTimeOfSleepForWeek()
        }

        return@withContext listFlow.map { list ->
            var totalTime = 0L

            for (sleep in list) {
                totalTime += sleep
            }

            if (list.isNotEmpty()){
                totalTime /= list.size
            }

            val hours = abs((totalTime) / 1000 / 60 / 60).toString()
            var minutes = abs((totalTime) / 1000 / 60 % 60).toString()
            if (minutes.length % 2 != 0)
                minutes = "0$minutes"

            "$hours:$minutes"
        }

    }

}