package com.example.domain.repository

interface StepsStorageRepository {

    suspend fun setTarget(target : Int)

    suspend fun getCurrentTarget() : Int

}