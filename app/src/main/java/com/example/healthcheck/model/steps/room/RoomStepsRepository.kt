package com.example.healthcheck.model.steps.room

import com.example.healthcheck.model.steps.StepsRepository
import com.example.healthcheck.model.steps.entities.Steps
import com.example.healthcheck.model.steps.room.entities.StepsDbEntity.Companion.fromSteps

class RoomStepsRepository(
    val stepsDao : StepsDao
) : StepsRepository{

    override suspend fun insertCountOfSteps(steps: Steps) {
        stepsDao.insertCountOfSteps(fromSteps(steps))
    }

    override fun getStepsForWeek(): List<Steps> {
        return stepsDao.getStepsForWeek().map { steps ->
            steps.toSteps()
        }
    }

    override fun getStepsForMonth(): List<Steps> {
        return stepsDao.getStepsForMonth().map { steps ->
            steps.toSteps()
        }
    }

}