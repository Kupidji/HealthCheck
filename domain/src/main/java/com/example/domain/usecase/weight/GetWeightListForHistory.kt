package com.example.domain.usecase.weight

import com.example.domain.AppDispatchers
import com.example.domain.models.Weight
import com.example.domain.repository.WeightRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class GetWeightListForHistory(private val repository: WeightRepository) {

    suspend fun execute() : Flow<List<Weight>> = withContext(AppDispatchers.io) {
        return@withContext repository.getListForHistory()
    }

}