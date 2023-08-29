package com.example.domain.usecase

import com.example.domain.AppDispatchers
import com.example.domain.repository.SleepRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.lang.Math.abs

class GetHoursOfSleepForDayFromDb(private val repository: SleepRepository) {

    suspend fun execute() : Flow<String> = withContext(AppDispatchers.default) {
        val timeFlow = withContext(AppDispatchers.io) {
            return@withContext repository.getTimeOfSleepForDay()
        }

        return@withContext timeFlow.map { time ->
            val hours = abs((time) / 1000 / 60 / 60).toString()
            var minutes = abs((time) / 1000 / 60 % 60).toString()
            if (minutes.length % 2 != 0)
                minutes = "0$minutes"

            "$hours:$minutes"
        }

    }

}