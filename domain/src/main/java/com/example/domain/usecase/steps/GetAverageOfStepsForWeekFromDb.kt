package com.example.domain.usecase.steps

import com.example.domain.AppDispatchers
import com.example.domain.repository.StepsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class GetAverageOfStepsForWeekFromDb(private val repository : StepsRepository) {

    fun execute() : Flow<Int> = flow<Int> {
        val listSize = withContext(AppDispatchers.io) {
            return@withContext repository.getStepsForMonth()
        }

        listSize.collect { list ->
            val getCountOfStepsForWeekFromDb = GetCountOfStepsForWeekFromDb(repository = repository)
            getCountOfStepsForWeekFromDb.execute().collect { countOfSteps ->
                if (list.isNotEmpty()) {
                    emit(countOfSteps / list.size)
                }
                else {
                    emit(0)
                }
            }
        }
    }.flowOn(AppDispatchers.default)

}