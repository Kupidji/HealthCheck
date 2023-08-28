package com.example.domain.usecase

import com.example.domain.AppDispatchers
import com.example.domain.repository.HeartRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class GetHeartForWeekFromDb(private val repository: HeartRepository) {

    suspend fun execute() : Flow<String> = withContext(AppDispatchers.default) {
        val listFlow = withContext(AppDispatchers.io) {
            return@withContext repository.getCardioForWeek()
        }
        var sumOfUpPressure = 0
        var sumOfDownPressure = 0
        var sumOfPulse = 0

        return@withContext listFlow.map { list ->
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
            "${sumOfUpPressure}/${sumOfDownPressure}/${sumOfPulse}"
        }

    }

}