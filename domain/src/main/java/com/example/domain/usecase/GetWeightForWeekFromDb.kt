package com.example.domain.usecase

import com.example.domain.AppDispatchers
import com.example.domain.repository.WeightRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class GetWeightForWeekFromDb(private val repository: WeightRepository) {

    suspend fun execute() : Flow<Float> = withContext(AppDispatchers.default) {
        val list = withContext(AppDispatchers.io) {
            return@withContext repository.getWeightForWeek()
        }
        return@withContext list.map { listOfWeight ->
            var sum = 0F
            for (weight in listOfWeight) {
                sum += weight
            }
            if (listOfWeight.isNotEmpty()) {
                sum/listOfWeight.size
            }
            else {
                0F
            }
        }
    }

}