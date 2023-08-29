package com.example.domain.usecase.weight

import com.example.domain.AppDispatchers
import com.example.domain.models.Weight
import com.example.domain.repository.WeightRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class GetLastWeightIdAndDate(private val repository: WeightRepository) {

    suspend fun execute() : Flow<Weight> = withContext(AppDispatchers.io) {
        return@withContext repository.getWeightLastIdAndDate().map { list ->
            var ourWeight = Weight(0,0F,0)
            for (weight in list) {
                ourWeight = weight
            }
            ourWeight
        }
    }

}