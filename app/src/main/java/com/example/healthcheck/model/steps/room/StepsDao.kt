package com.example.healthcheck.model.steps.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.healthcheck.model.steps.room.entities.StepsDbEntity
import com.example.healthcheck.model.steps.room.entities.countOfStepsTupel

@Dao
interface StepsDao {

    @Insert
    suspend fun insertCountOfSteps(stepsDbEntity: StepsDbEntity)

    @Query("SELECT * FROM steps order by id desc limit 7")
    suspend fun getStepsForWeek() : List<StepsDbEntity>

    @Query("SELECT * FROM steps order by id desc limit 30")
    suspend fun getStepsForMonth() : List<StepsDbEntity>

    @Query("SELECT * FROM steps order by id desc limit 1")
    suspend fun getLastDate() : StepsDbEntity
    @Update
    suspend fun updateCountOfSteps(stepsDbEntity : StepsDbEntity)

}