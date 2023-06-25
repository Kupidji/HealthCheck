package com.example.healthcheck.model.steps

import com.example.healthcheck.model.steps.entities.Steps
import com.example.healthcheck.model.steps.room.entities.StepsDbEntity

interface StepsRepository {

    suspend fun insertCountOfSteps(steps : Steps)

    suspend fun getStepsForWeek() : List<Int>

    suspend fun getStepsForMonth() : List<Int>

    suspend fun updateCountOfSteps(steps: Steps)

    suspend fun getLastDate() : StepsDbEntity

}