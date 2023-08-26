package com.example.domain.usecase

import com.example.domain.AppDispatchers
import com.example.domain.repository.HeartRepository
import kotlinx.coroutines.withContext

class GetHeartForMonthFromDb(private val repository: HeartRepository) {

    suspend fun execute() : String = withContext(AppDispatchers.default) {
        val list = withContext(AppDispatchers.io) {
            return@withContext repository.getCardioForMonth()
        }
        var sumOfUpPressure = 0
        var sumOfDownPressure = 0
        var sumOfPulse = 0

        for (heart in list) {
            sumOfUpPressure += heart.pressureUp
            sumOfDownPressure += heart.pressureDown
            sumOfPulse += heart.pulse
        }

        sumOfUpPressure /= list.size
        sumOfDownPressure /= list.size
        sumOfPulse /= list.size

        return@withContext "${sumOfUpPressure}/${sumOfDownPressure}/${sumOfPulse}"
    }

}