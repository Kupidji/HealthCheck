package com.example.domain.repository

import com.example.domain.models.Heart
import kotlinx.coroutines.flow.Flow

interface HeartRepository {

    suspend fun insertCardio(heart : Heart)

    suspend fun updateCardio(heart: Heart)

    suspend fun deleteCardio(heart: Heart)

    fun getCardioForDay() : Flow<List<Heart>>

    fun getCardioForWeek() : Flow<List<Heart>>

    fun getCardioForMonth() : Flow<List<Heart>>

    fun getListCardioForLastNote() : Flow<List<Heart>>

    fun getListCardioForHistory() : Flow<List<Heart>>

}