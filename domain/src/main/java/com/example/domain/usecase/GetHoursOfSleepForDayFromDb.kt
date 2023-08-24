package com.example.domain.usecase

import com.example.domain.AppDispatchers
import com.example.domain.repository.SleepRepository
import kotlinx.coroutines.withContext
import java.lang.Math.abs

class GetHoursOfSleepForDayFromDb(private val repository: SleepRepository) {

    suspend fun execute() : String = withContext(AppDispatchers.default) {
        val time = withContext(AppDispatchers.io) {
            return@withContext repository.getTimeOfSleepForDay()
        }

        val hours = abs((time) / 1000 / 60 / 60).toString()
        var minutes = abs((time) / 1000 / 60 % 60).toString()
        if (minutes.length % 2 != 0)
            minutes = "0$minutes"

        return@withContext "$hours:$minutes"
    }

}