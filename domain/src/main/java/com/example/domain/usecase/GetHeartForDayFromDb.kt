package com.example.domain.usecase

import com.example.domain.AppDispatchers
import com.example.domain.repository.HeartRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class GetHeartForDayFromDb(private val repository: HeartRepository) {

    suspend fun execute() : Flow<String> = withContext(AppDispatchers.default) {
        val heartFlow = withContext(AppDispatchers.io) {
            repository.getCardioForDay()
        }

        return@withContext heartFlow.map { heart ->
            "${heart.pressureUp}/${heart.pressureDown}/${heart.pulse}"
        }
    }

}