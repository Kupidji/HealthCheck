package com.example.domain.usecase.weight

import com.example.domain.AppDispatchers
import com.example.domain.repository.WeightStorageRepository
import kotlinx.coroutines.withContext

class SetNeck(private val repository: WeightStorageRepository) {

    suspend fun execute(neck : Float) = withContext(AppDispatchers.io) {
        repository.setNeck(neck = neck)
    }

}