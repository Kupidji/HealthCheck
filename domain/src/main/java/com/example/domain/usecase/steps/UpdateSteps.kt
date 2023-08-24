package com.example.domain.usecase.steps

import com.example.domain.AppDispatchers
import com.example.domain.models.Steps
import com.example.domain.repository.StepsRepository
import kotlinx.coroutines.withContext

class UpdateSteps(private val repository : StepsRepository) {

    suspend fun execute(steps : Steps) = withContext(AppDispatchers.io) {
        repository.updateCountOfSteps(steps)
    }

}