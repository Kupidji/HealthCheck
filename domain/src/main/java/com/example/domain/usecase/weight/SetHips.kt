package com.example.domain.usecase.weight

import com.example.domain.AppDispatchers
import com.example.domain.repository.WeightStorageRepository
import kotlinx.coroutines.withContext

class SetHips(private val repository: WeightStorageRepository) {

    suspend fun execute(hips : Float) = withContext(AppDispatchers.io) {
        repository.setBothHips(hips = hips)
    }

}