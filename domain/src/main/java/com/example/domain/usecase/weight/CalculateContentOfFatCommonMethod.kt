package com.example.domain.usecase.weight

import com.example.domain.AppDispatchers
import com.example.domain.repository.ProfileStorageRepository
import com.example.domain.repository.WeightRepository
import kotlinx.coroutines.withContext

class CalculateContentOfFatCommonMethod(private val repositoryProfile: ProfileStorageRepository, private val repositoryWeight: WeightRepository) {

    suspend fun execute() : Float = withContext(AppDispatchers.default) {
        val bodyMassIndex = CalculateBodyMassIndex(repositoryProfile = repositoryProfile, repositoryWeight = repositoryWeight)

        val gender = withContext(AppDispatchers.io) {
            return@withContext repositoryProfile.getGender()
        }

        val age = withContext(AppDispatchers.io) {
            return@withContext repositoryProfile.getAge()
        }

        return@withContext if (gender) {
            (1.2 * bodyMassIndex.execute() + (0.23 * age) - (10.8 * 1) - 5.4).toFloat()
        } else {
            (1.2 * bodyMassIndex.execute() + (0.23 * age) - (10.8 * 0) - 5.4).toFloat()
        }

    }

}