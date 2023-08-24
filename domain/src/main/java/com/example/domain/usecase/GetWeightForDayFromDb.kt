package com.example.domain.usecase

import com.example.domain.AppDispatchers
import com.example.domain.repository.WeightRepository
import kotlinx.coroutines.withContext

class GetWeightForDayFromDb(private val repository : WeightRepository) {

    suspend fun execute() : Float = withContext(AppDispatchers.io) {
        return@withContext repository.getWeightForDay().weight
    }

}