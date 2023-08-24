package com.example.domain.repository

interface WeightStorageRepository {

    suspend fun getTarget() : Float

    suspend fun setTarget(target : Float)

    suspend fun getNeck() : Float

    suspend fun getWaist() : Float

    suspend fun getForearm() : Float

    suspend fun getWrist() : Float

    suspend fun getBothHips() : Float

    suspend fun getHip1() : Float

    suspend fun getHip2() : Float

    suspend fun getShin() : Float

    suspend fun setNeck(neck : Float)

    suspend fun setWaist(waist : Float)

    suspend fun setForearm(foream : Float)

    suspend fun setWrist(wrist : Float)

    suspend fun setBothHips(hips : Float)

    suspend fun setHip1(hip : Float)

    suspend fun setHip2(hip : Float)

    suspend fun setShin(shin : Float)

}