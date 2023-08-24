package com.example.domain.usecase.steps

import com.example.domain.AppDispatchers
import com.example.domain.repository.StepsStorageRepository
import kotlinx.coroutines.withContext

class GetStepsTarget(private val repository: StepsStorageRepository) {

    suspend fun execute() : Int = withContext(AppDispatchers.io) {
        return@withContext repository.getCurrentTarget()
    }

}