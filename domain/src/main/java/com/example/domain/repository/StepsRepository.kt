package com.example.domain.repository

import com.example.domain.models.Steps
import kotlinx.coroutines.flow.Flow


interface StepsRepository {

    suspend fun insertCountOfSteps(steps : Steps)

    suspend fun updateCountOfSteps(steps: Steps)

    fun getStepsForDay() : Flow<List<Int>>

    fun getStepsForWeek() : Flow<List<Int>>

    fun getStepsForMonth() : Flow<List<Int>>

    fun getListForHistory() : Flow<List<Steps>>

    fun getLastStepsEntity() : Flow<List<Steps>>

}