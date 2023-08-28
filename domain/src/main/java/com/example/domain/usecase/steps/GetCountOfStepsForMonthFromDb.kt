package com.example.domain.usecase.steps

import com.example.domain.AppDispatchers
import com.example.domain.repository.StepsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class GetCountOfStepsForMonthFromDb(private val repository: StepsRepository) {

    suspend fun execute() : Flow<Int> = withContext(AppDispatchers.default) {
        val list = withContext(AppDispatchers.io) {
            return@withContext repository.getStepsForWeek()
        }
        return@withContext list.map { listOfSteps ->
            var sum = 0
            for (steps in listOfSteps) {
                sum += steps
            }
            sum
        }
    }

}