package com.example.domain.usecase

import com.example.domain.AppDispatchers
import com.example.domain.repository.WeightRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class GetWeightForDayFromDb(private val repository : WeightRepository) {

    suspend fun execute() : Flow<Float> = withContext(AppDispatchers.io) {
        return@withContext repository.getWeightForDay().map { list ->
            var ourWeight = 0F
            for (weight in list) {
                ourWeight = weight
            }
            ourWeight
        }
    }

}