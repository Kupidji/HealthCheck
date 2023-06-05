package com.example.healthcheck.model.steps

import com.example.healthcheck.model.steps.entities.Steps

interface StepsRepository {

    suspend fun insertCountOfSteps(steps : Steps)

    fun getStepsForWeek() : List<Steps>

    fun getStepsForMonth() : List<Steps>

}