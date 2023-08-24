package com.example.domain.usecase.weight

import com.example.domain.AppDispatchers
import com.example.domain.models.Weight
import com.example.domain.repository.WeightRepository
import kotlinx.coroutines.withContext

class InsertWeight(private val repository: WeightRepository) {

    suspend fun execute(weight : Weight) = withContext(AppDispatchers.io) {
        repository.insertWeight(weight)
    }

}