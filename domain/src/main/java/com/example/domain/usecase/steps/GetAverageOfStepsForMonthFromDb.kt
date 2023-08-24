package com.example.domain.usecase.steps

import com.example.domain.AppDispatchers
import com.example.domain.repository.StepsRepository
import kotlinx.coroutines.withContext

class GetAverageOfStepsForMonthFromDb(private val repository : StepsRepository) {

    suspend fun execute() : Int = withContext(AppDispatchers.default) {
        val sizeListOfSteps = withContext(AppDispatchers.io) {
            return@withContext repository.getStepsForMonth().size
        }
        val getCountOfStepsForMonthFromDb = GetCountOfStepsForMonthFromDb(repository = repository)
        return@withContext getCountOfStepsForMonthFromDb.execute() / sizeListOfSteps
    }

}