package com.example.domain.usecase.heart

import com.example.domain.AppDispatchers
import com.example.domain.models.Heart
import com.example.domain.repository.HeartRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class GetListOfHeartForHistory(private val repository: HeartRepository) {

    suspend fun execute() : Flow<List<Heart>> = withContext(AppDispatchers.io) {
        return@withContext repository.getListCardioForHistory()
    }

}