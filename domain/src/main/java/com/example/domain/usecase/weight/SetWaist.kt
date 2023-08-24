package com.example.domain.usecase.weight

import com.example.domain.AppDispatchers
import com.example.domain.repository.WeightStorageRepository
import kotlinx.coroutines.withContext

class SetWaist(private val repository: WeightStorageRepository) {

    suspend fun execute(waist : Float) = withContext(AppDispatchers.io) {
        repository.setWaist(waist = waist)
    }

}