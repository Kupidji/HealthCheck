package com.example.domain.usecase.weight

import com.example.domain.AppDispatchers
import com.example.domain.repository.WeightStorageRepository
import kotlinx.coroutines.withContext

class GetForearm(private val repository: WeightStorageRepository) {

    suspend fun execute() : Float = withContext(AppDispatchers.io) {
        return@withContext repository.getForearm()
    }

}