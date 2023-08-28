package com.example.domain.usecase.weight

import com.example.domain.AppDispatchers
import com.example.domain.repository.ProfileStorageRepository
import com.example.domain.repository.WeightRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class CalculateContentOfFatCommonMethod(private val repositoryProfile: ProfileStorageRepository, private val repositoryWeight: WeightRepository) {

    suspend fun execute() : Flow<Float> = withContext(AppDispatchers.default) {
        val gender = withContext(AppDispatchers.io) {
            return@withContext repositoryProfile.getGender()
        }

        val age = withContext(AppDispatchers.io) {
            return@withContext repositoryProfile.getAge()
        }

        val bodyMassIndex = withContext(AppDispatchers.io) {
            val calculateBodyMassIndex = CalculateBodyMassIndex(repositoryProfile = repositoryProfile, repositoryWeight = repositoryWeight)
            return@withContext calculateBodyMassIndex.execute()
        }

        return@withContext bodyMassIndex.map { mass ->
            if (gender) {
                (1.2 * mass + (0.23 * age) - (10.8 * 1) - 5.4).toFloat()
            } else {
                (1.2 * mass + (0.23 * age) - (10.8 * 0) - 5.4).toFloat()
            }
        }

    }

}