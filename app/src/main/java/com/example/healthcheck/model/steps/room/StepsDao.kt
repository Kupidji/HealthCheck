package com.example.healthcheck.model.steps.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.healthcheck.model.steps.room.entities.StepsDbEntity
import com.example.healthcheck.model.steps.room.entities.countOfStepsTupel

@Dao
interface StepsDao {

    @Insert
    suspend fun insertCountOfSteps(stepsDbEntity: StepsDbEntity)

    //TODO доделать запрос для вытягивание инфы за неделю
    @Query("SELECT * FROM steps")
    suspend fun getStepsForWeek() : List<StepsDbEntity>

    //TODO доделать запрос для вытягивание инфы за месяц
    @Query("SELECT * FROM steps")
    suspend fun getStepsForMonth() : List<StepsDbEntity>

}