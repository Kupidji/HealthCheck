package com.example.data.steps.room

import com.example.data.steps.entities.StepsForDb
import com.example.data.steps.room.entities.StepsDbEntity.Companion.forUpdate
import com.example.data.steps.room.entities.StepsDbEntity.Companion.fromSteps
import com.example.domain.models.Steps
import com.example.domain.repository.StepsRepository
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn

class RoomStepsRepository(
    val stepsDao : StepsDao
) : StepsRepository {

    override suspend fun insertCountOfSteps(steps: Steps) {
        stepsDao.insertCountOfSteps(fromSteps(toStepsForDb(steps)))
    }

    override suspend fun updateCountOfSteps(steps: Steps) {
        stepsDao.updateCountOfSteps(forUpdate(toStepsForDb(steps)))
    }

    override fun getStepsForDay(): Flow<Int> {
        return stepsDao.getStepsForDay().map { steps ->
            steps.toSteps().countOfSteps
        }
    }

    override fun getStepsForWeek(): Flow<List<Int>> {
        return stepsDao.getStepsForWeek().map { list ->
            list.map { steps ->
                steps.toSteps().countOfSteps
            }
        }

    }

    override fun getStepsForMonth(): Flow<List<Int>> {
        return stepsDao.getStepsForMonth().map { list ->
            list.map { steps ->
                steps.toSteps().countOfSteps
            }
        }

    }

    override fun getListForHistory(): Flow<List<Steps>> {
        return stepsDao.getListForHistory().map { list ->
            list.map { stepsDbEntity ->
                Steps (
                    id = stepsDbEntity.id,
                    countOfSteps = stepsDbEntity.countOfSteps,
                    date = stepsDbEntity.date
                )
            }
        }

    }

    override fun getLastStepsEntity(): Flow<Steps> {
        return stepsDao.getLastStepsEntity().map { stepsDbEntity ->
            Steps (
                id = stepsDbEntity.id,
                countOfSteps = stepsDbEntity.countOfSteps,
                date = stepsDbEntity.date
            )
        }
    }

    private fun toStepsForDb(steps : Steps) : StepsForDb {
        return StepsForDb(
            id = steps.id,
            countOfSteps = steps.countOfSteps,
            date = steps.date,
        )
    }
}