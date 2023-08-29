package com.example.domain.usecase.steps

import com.example.domain.AppDispatchers
import com.example.domain.models.Steps
import com.example.domain.repository.StepsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class GetLastStepsIdAndDate(private val repository: StepsRepository) {

    suspend fun execute() : Flow<Steps> = withContext(AppDispatchers.io){
        return@withContext repository.getLastStepsEntity().map { list ->
            var ourSteps = Steps(0,0,0)
            for (steps in list) {
                ourSteps = steps
            }
            ourSteps
        }
    }

}