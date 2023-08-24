package com.example.domain.usecase.sleep

import com.example.domain.AppDispatchers
import com.example.domain.repository.SleepRepository
import kotlinx.coroutines.withContext
import java.lang.Math.abs

class GetHoursOfSleepForMonthFromDb(private val repository: SleepRepository) {

    suspend fun execute() : String = withContext(AppDispatchers.default) {
        val list = withContext(AppDispatchers.io) {
            return@withContext repository.getTimeOfSleepForMonth()
        }

        var totalTime = 0L
        for (timeOfSleep in list) {
            totalTime += timeOfSleep
        }

        val hours = abs((totalTime) / 1000 / 60 / 60).toString()
        var minutes = abs((totalTime) / 1000 / 60 % 60).toString()
        if (minutes.length % 2 != 0)
            minutes = "0$minutes"

        return@withContext "$hours:$minutes"
    }

}