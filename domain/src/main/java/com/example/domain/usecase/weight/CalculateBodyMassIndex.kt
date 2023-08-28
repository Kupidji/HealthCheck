package com.example.domain.usecase.weight

import com.example.domain.AppDispatchers
import com.example.domain.repository.ProfileStorageRepository
import com.example.domain.repository.WeightRepository
import com.example.domain.usecase.GetWeightForMonthFromDb
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class CalculateBodyMassIndex(private val repositoryProfile: ProfileStorageRepository, private val repositoryWeight: WeightRepository) {

    suspend fun execute() : Flow<Float> = withContext(AppDispatchers.default) {
        val height = withContext(AppDispatchers.io) {
            return@withContext repositoryProfile.getHeight()
        }

        val weightFlow = withContext(AppDispatchers.io) {
            val getWeightForMonthFromDb = GetWeightForMonthFromDb(repository = repositoryWeight)
            return@withContext getWeightForMonthFromDb.execute()
        }

        return@withContext weightFlow.map { weight ->
            ((10000 * weight) / (height * height))
        }

    }

}