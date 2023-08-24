package com.example.domain.usecase.weight

import com.example.domain.AppDispatchers
import com.example.domain.repository.WeightStorageRepository
import kotlinx.coroutines.withContext

class SetWrist(private val repository: WeightStorageRepository) {

    suspend fun execute(wrist : Float) = withContext(AppDispatchers.io) {
        repository.setWrist(wrist)
    }

}