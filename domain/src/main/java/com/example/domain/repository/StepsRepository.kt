package com.example.domain.repository

import com.example.domain.models.Steps
import kotlinx.coroutines.flow.SharedFlow


interface StepsRepository {

    suspend fun insertCountOfSteps(steps : Steps)

    suspend fun updateCountOfSteps(steps: Steps)

    suspend fun getStepsForDay() : Int

    suspend fun getStepsForWeek() : List<Int>

    suspend fun getStepsForMonth() : List<Int>

    suspend fun getLastDate() : Steps

}