package com.example.domain.usecase.weight

import com.example.domain.AppDispatchers
import com.example.domain.repository.WeightStorageRepository
import kotlinx.coroutines.withContext

class SetForearm(private val repository: WeightStorageRepository) {

    suspend fun execute(forearm : Float) = withContext(AppDispatchers.io) {
        repository.setForearm(foream = forearm)
    }

}