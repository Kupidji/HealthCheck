package com.example.domain.usecase.steps

import com.example.domain.AppDispatchers
import com.example.domain.repository.StepsRepository
import kotlinx.coroutines.withContext

class GetCountOfStepsForMonthFromDb(private val repository: StepsRepository) {

    suspend fun execute() = withContext(AppDispatchers.default) {
        val list = withContext(AppDispatchers.io) {
            return@withContext repository.getStepsForMonth()
        }

        var sum = 0
        for (countOfSteps in list) {
            sum += countOfSteps
        }

        return@withContext sum
    }

}