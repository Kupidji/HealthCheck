package com.example.domain.usecase.sleep

import com.example.domain.AppDispatchers
import com.example.domain.repository.SleepRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class GetHoursOfSleepForWeekFromDb(private val repository : SleepRepository) {

    suspend fun execute() : Flow<String> = withContext(AppDispatchers.default) {
        val listFlow = withContext(AppDispatchers.io) {
            return@withContext repository.getTimeOfSleepForWeek()
        }

        return@withContext listFlow.map { list ->
            var totalTime = 0L
            for (timeOfSleep in list) {
                totalTime += timeOfSleep
            }

            val hours = Math.abs((totalTime) / 1000 / 60 / 60).toString()
            var minutes = Math.abs((totalTime) / 1000 / 60 % 60).toString()
            if (minutes.length % 2 != 0)
                minutes = "0$minutes"

            "$hours:$minutes"
        }

    }

}