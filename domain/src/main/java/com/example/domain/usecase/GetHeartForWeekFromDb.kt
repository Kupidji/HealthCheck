package com.example.domain.usecase

import com.example.domain.AppDispatchers
import com.example.domain.repository.HeartRepository
import kotlinx.coroutines.withContext

class GetHeartForWeekFromDb(private val repository: HeartRepository) {

    suspend fun execute() : String = withContext(AppDispatchers.default) {
        val list = withContext(AppDispatchers.io) {
            return@withContext repository.getCardioForWeek()
        }
        var sumOfUpPressure = 0
        var sumOfDownPressure = 0
        var sumOfPulse = 0

        for (heart in list) {
            sumOfUpPressure += heart.pressureUp
            sumOfDownPressure += heart.pressureDown
            sumOfPulse += heart.pulse
        }

        if (list.isNotEmpty()) {
            sumOfUpPressure /= list.size
            sumOfDownPressure /= list.size
            sumOfPulse /= list.size
        }

        return@withContext "${sumOfUpPressure}/${sumOfDownPressure}/${sumOfPulse}"
    }

}