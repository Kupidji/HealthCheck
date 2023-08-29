package com.example.domain.usecase.steps

import com.example.domain.AppDispatchers
import com.example.domain.repository.StepsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class GetCountOfStepsForDayFromDb(private val repository : StepsRepository) {

    suspend fun execute() : Flow<Int> = withContext(AppDispatchers.io) {
        return@withContext repository.getStepsForDay().map { list ->
            var ourSteps = 0
            for (steps in list) {
                ourSteps = steps
            }
            ourSteps
        }
    }

}