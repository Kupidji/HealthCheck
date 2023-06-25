package com.example.healthcheck.model.heart

import com.example.healthcheck.model.heart.entities.Heart

interface HeartRepository {

    suspend fun insertCardio(heart : Heart)

    fun getCardioForDay() : List<Heart>

    suspend fun updateCardio(heart: Heart)

}