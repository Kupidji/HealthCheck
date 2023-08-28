package com.example.data.steps.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.data.steps.room.entities.StepsDbEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface StepsDao {

    @Insert
    suspend fun insertCountOfSteps(stepsDbEntity: StepsDbEntity)

    @Query("SELECT * FROM steps order by id desc limit 1")
    fun getStepsForDay() : Flow<StepsDbEntity>

    @Query("SELECT * FROM steps order by id desc limit 7")
    fun getStepsForWeek() : Flow<List<StepsDbEntity>>

    @Query("SELECT * FROM steps order by id desc limit 30")
    fun getStepsForMonth() : Flow<List<StepsDbEntity>>

    @Query("SELECT * FROM steps")
    fun getListForHistory() : Flow<List<StepsDbEntity>>

    @Query("SELECT * FROM steps order by id desc limit 1")
    fun getLastStepsEntity() : Flow<StepsDbEntity>

    @Update
    suspend fun updateCountOfSteps(stepsDbEntity : StepsDbEntity)

}