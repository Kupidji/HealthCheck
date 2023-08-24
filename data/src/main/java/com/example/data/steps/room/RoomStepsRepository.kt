package com.example.data.steps.room

import com.example.data.steps.entities.StepsForDb
import com.example.data.steps.room.entities.StepsDbEntity.Companion.forUpdate
import com.example.data.steps.room.entities.StepsDbEntity.Companion.fromSteps
import com.example.domain.models.Steps
import com.example.domain.repository.StepsRepository
import kotlinx.coroutines.flow.map

class RoomStepsRepository(
    val stepsDao : StepsDao
) : StepsRepository {

    override suspend fun insertCountOfSteps(steps: Steps) {
        stepsDao.insertCountOfSteps(fromSteps(toStepsForDb(steps)))
    }

    override suspend fun updateCountOfSteps(steps: Steps) {
        stepsDao.updateCountOfSteps(forUpdate(toStepsForDb(steps)))
    }

    override suspend fun getStepsForDay(): Int {
        return stepsDao.getStepsForDay().countOfSteps
    }

    override suspend fun getStepsForWeek(): List<Int> {
        var list = mutableListOf<Int>()
        stepsDao.getStepsForWeek().map { steps ->
            steps.toSteps()
            list.add(steps.countOfSteps)
        }
        return list
//        var result = MutableSharedFlow<List<Int>>(1, 0, BufferOverflow.DROP_OLDEST)
//        var list = mutableListOf<Int>()
//        stepsDao.getStepsForWeek().collect { listOfSteps ->
//            listOfSteps.map { steps ->
//                steps.toSteps()
//                //result.emit()
//                list.add(steps.countOfSteps)
//            }
//        }
//        result.emit(list)
//        return result
    }

    override suspend fun getStepsForMonth(): List<Int> {
        var list = mutableListOf<Int>()
        stepsDao.getStepsForMonth().map { steps ->
            steps.toSteps()
            list.add(steps.countOfSteps)
        }
        return list
//        var result = MutableSharedFlow<List<Int>>(1, 0, BufferOverflow.DROP_OLDEST)
//        var list = mutableListOf<Int>()
//        stepsDao.getStepsForMonth().collect { listOfSteps ->
//            listOfSteps.map { steps ->
//                steps.toSteps()
//                //result.emit()
//                list.add(steps.countOfSteps)
//            }
//        }
//        result.emit(list)
//        return result
    }

    override suspend fun getLastDate(): Steps {
        val steps = stepsDao.getLastDate()
        return Steps (
            id = steps.id,
            countOfSteps = steps.countOfSteps,
            date = steps.date,
        )
    }

    private fun toStepsForDb(steps : Steps) : StepsForDb {
        return StepsForDb(
            id = steps.id,
            countOfSteps = steps.countOfSteps,
            date = steps.date,
        )
    }
}