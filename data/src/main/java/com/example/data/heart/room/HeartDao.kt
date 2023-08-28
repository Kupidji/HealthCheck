package com.example.data.heart.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.data.heart.room.entities.HeartDbEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HeartDao {

    @Insert
    suspend fun insertCardio(heartDbEntity: HeartDbEntity)

    @Update
    suspend fun updateCardio(heartDbEntity: HeartDbEntity)

    @Delete
    suspend fun deleteCardio(heartDbEntity: HeartDbEntity)

    @Query("SELECT * FROM heart ORDER BY id DESC LIMIT 1")
    fun getCardioForDay() : Flow<HeartDbEntity>

    @Query("SELECT * FROM heart ORDER BY id DESC LIMIT 7")
    fun getCadrioForWeek() : Flow<List<HeartDbEntity>>

    @Query("SELECT * FROM heart ORDER BY id DESC LIMIT 30")
    fun getCadrioForMonth() : Flow<List<HeartDbEntity>>

    @Query("SELECT * FROM heart")
    fun getCadrioForHistory() : Flow<List<HeartDbEntity>>

}