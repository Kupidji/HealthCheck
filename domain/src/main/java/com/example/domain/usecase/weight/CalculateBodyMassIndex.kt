package com.example.domain.usecase.weight

import com.example.domain.AppDispatchers
import com.example.domain.repository.ProfileStorageRepository
import com.example.domain.repository.WeightRepository
import com.example.domain.usecase.GetWeightForMonthFromDb
import kotlinx.coroutines.withContext

class CalculateBodyMassIndex(private val repositoryProfile: ProfileStorageRepository, private val repositoryWeight: WeightRepository) {

    suspend fun execute() : Float = withContext(AppDispatchers.default) {

        val height = withContext(AppDispatchers.io) {
            return@withContext repositoryProfile.getHeight()
        }

        val weight = withContext(AppDispatchers.io) {
            val getWeightForMonthFromDb = GetWeightForMonthFromDb(repository = repositoryWeight)
            return@withContext getWeightForMonthFromDb.execute()
        }

        return@withContext ((10000 * weight) / (height * height))
    }

}