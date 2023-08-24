package com.example.domain.usecase.weight

import com.example.domain.AppDispatchers
import com.example.domain.repository.WeightStorageRepository
import kotlinx.coroutines.withContext

class SetWeightTarget(private val repository: WeightStorageRepository) {

    suspend fun execute(target : Float) = withContext(AppDispatchers.io) {
        repository.setTarget(target = target)
    }

}