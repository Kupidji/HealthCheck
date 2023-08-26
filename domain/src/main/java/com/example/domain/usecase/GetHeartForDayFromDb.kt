package com.example.domain.usecase

import com.example.domain.AppDispatchers
import com.example.domain.repository.HeartRepository
import kotlinx.coroutines.withContext

class GetHeartForDayFromDb(private val repository: HeartRepository) {

    suspend fun execute() : String = withContext(AppDispatchers.io) {
        try {
            val heart = repository.getCardioForDay()
            return@withContext "${heart.pressureUp}/${heart.pressureDown}/${heart.pulse}"
        }
        catch (e : NullPointerException) {
            return@withContext ""
        }

    }

}