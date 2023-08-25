package com.example.domain.repository

import com.example.domain.models.Heart

interface HeartRepository {

    suspend fun insertCardio(heart : Heart)

    fun getCardioForDay() : List<Heart>

    suspend fun updateCardio(heart: Heart)

    suspend fun deleteCardio(heart: Heart)

    suspend fun getListCardioForLastNote() : List<Heart>

    suspend fun getListCardioForHistory() : List<Heart>

}