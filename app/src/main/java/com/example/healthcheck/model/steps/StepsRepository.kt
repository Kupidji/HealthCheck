package com.example.healthcheck.model.steps

import com.example.healthcheck.model.steps.entities.Steps
import com.example.healthcheck.model.steps.room.entities.countOfStepsTupel

interface StepsRepository {

    suspend fun insertCountOfSteps(steps : Steps)

    suspend fun getStepsForWeek() : List<Int>

    suspend fun getStepsForMonth() : List<Steps>

}