package com.example.domain.usecase.weight

import com.example.domain.AppDispatchers
import com.example.domain.repository.WeightStorageRepository
import kotlinx.coroutines.withContext

class SetShin(private val repository: WeightStorageRepository) {

    suspend fun execute(shin : Float) = withContext(AppDispatchers.io) {
        repository.setShin(shin = shin)
    }

}