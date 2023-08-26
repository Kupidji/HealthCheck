package com.example.domain.usecase.steps

import com.example.domain.AppDispatchers
import com.example.domain.repository.StepsRepository
import kotlinx.coroutines.withContext

class GetAverageOfStepsForWeekFromDb(private val repository : StepsRepository) {

    suspend fun execute() : Int = withContext(AppDispatchers.default) {
        val sizeListOfSteps = withContext(AppDispatchers.io) {
            return@withContext repository.getStepsForWeek().size
        }
        val getCountOfStepsForWeekFromDb = GetCountOfStepsForWeekFromDb(repository = repository)
        val totalCountOfStepsForWeek = getCountOfStepsForWeekFromDb.execute()
        if (sizeListOfSteps != 0) {
            return@withContext totalCountOfStepsForWeek / sizeListOfSteps
        }
        else {
            return@withContext 0
        }

    }

}