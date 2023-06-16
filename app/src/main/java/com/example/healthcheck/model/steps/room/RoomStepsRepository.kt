package com.example.healthcheck.model.steps.room

import androidx.lifecycle.MutableLiveData
import com.example.healthcheck.model.steps.StepsRepository
import com.example.healthcheck.model.steps.entities.Steps
import com.example.healthcheck.model.steps.room.entities.StepsDbEntity
import com.example.healthcheck.model.steps.room.entities.StepsDbEntity.Companion.forUpdate
import com.example.healthcheck.model.steps.room.entities.StepsDbEntity.Companion.fromSteps
import com.example.healthcheck.model.steps.room.entities.countOfStepsTupel

class RoomStepsRepository(
    val stepsDao : StepsDao
) : StepsRepository{

    override suspend fun insertCountOfSteps(steps: Steps) {
        stepsDao.insertCountOfSteps(fromSteps(steps))
    }

    override suspend fun getStepsForWeek(): List<Int> {
        var list = mutableListOf<Int>()
        var new = stepsDao.getStepsForWeek().map { steps ->
            steps.toSteps()
            list.add(steps.countOfSteps)
        }
        return list
    }

    override suspend fun getStepsForMonth(): List<Int> {
        var list = mutableListOf<Int>()
        var new = stepsDao.getStepsForMonth().map { steps ->
            steps.toSteps()
            list.add(steps.countOfSteps)
        }
        return list
    }

    override suspend fun updateCountOfSteps(steps: Steps) {
        stepsDao.updateCountOfSteps(forUpdate(steps))
    }

    override suspend fun getLastDate(): StepsDbEntity {
        return stepsDao.getLastDate()
    }

}