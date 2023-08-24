package com.example.domain.usecase.weight

import com.example.domain.AppDispatchers
import com.example.domain.repository.WeightStorageRepository
import kotlinx.coroutines.withContext

class SetRightHip(private val repository: WeightStorageRepository) {

    suspend fun execute(hip : Float) = withContext(AppDispatchers.io) {
        repository.setHip2(hip)
    }

}