package com.example.data.weight

import android.content.Context
import com.example.data.FOREARM
import com.example.data.HIPS
import com.example.data.HIP_1
import com.example.data.HIP_2
import com.example.data.NECK
import com.example.data.SHIN
import com.example.data.WAIST
import com.example.data.WEIGHT
import com.example.data.WEIGHT_TARGET
import com.example.data.WRIST
import com.example.domain.AppDispatchers
import com.example.domain.repository.WeightStorageRepository
import kotlinx.coroutines.withContext

class SharedPrefWeightStorage(private val context : Context) : WeightStorageRepository {

    private val storage = context.applicationContext.getSharedPreferences(WEIGHT, Context.MODE_PRIVATE)
    private val storageEdit = context.applicationContext.getSharedPreferences(WEIGHT, Context.MODE_PRIVATE).edit()
    override suspend fun getTarget(): Float = withContext(AppDispatchers.io) {
        return@withContext storage.getFloat(WEIGHT_TARGET, 120F)
    }

    override suspend fun setTarget(target: Float) = withContext(AppDispatchers.io) {
        storageEdit.putFloat(WEIGHT_TARGET, target).apply()
    }

    override suspend fun getNeck(): Float = withContext(AppDispatchers.io) {
        return@withContext storage.getFloat(NECK, 0F)
    }

    override suspend fun getWaist(): Float = withContext(AppDispatchers.io) {
        return@withContext storage.getFloat(WAIST, 0F)
    }

    override suspend fun getForearm(): Float = withContext(AppDispatchers.io) {
        return@withContext storage.getFloat(FOREARM, 0F)
    }

    override suspend fun getWrist(): Float = withContext(AppDispatchers.io) {
        return@withContext storage.getFloat(WRIST, 0F)
    }

    override suspend fun getBothHips(): Float = withContext(AppDispatchers.io) {
        return@withContext storage.getFloat(HIPS, 0F)
    }

    override suspend fun getHip1(): Float = withContext(AppDispatchers.io) {
        return@withContext storage.getFloat(HIP_1, 0F)
    }

    override suspend fun getHip2(): Float = withContext(AppDispatchers.io) {
        return@withContext storage.getFloat(HIP_2, 0F)
    }

    override suspend fun getShin(): Float = withContext(AppDispatchers.io) {
        return@withContext storage.getFloat(SHIN, 0F)
    }

    override suspend fun setNeck(neck: Float) : Unit = withContext(AppDispatchers.io) {
        storageEdit.putFloat(NECK, neck).apply()
    }

    override suspend fun setWaist(waist: Float) : Unit = withContext(AppDispatchers.io) {
        storageEdit.putFloat(WAIST, waist).apply()
    }

    override suspend fun setForearm(foream: Float) : Unit = withContext(AppDispatchers.io) {
        storageEdit.putFloat(FOREARM, foream).apply()
    }

    override suspend fun setWrist(wrist: Float) : Unit = withContext(AppDispatchers.io) {
        storageEdit.putFloat(WRIST, wrist).apply()
    }

    override suspend fun setBothHips(hips: Float) : Unit = withContext(AppDispatchers.io) {
        storageEdit.putFloat(HIPS, hips).apply()
    }

    override suspend fun setHip1(hip: Float) : Unit = withContext(AppDispatchers.io) {
        storageEdit.putFloat(HIP_1, hip).apply()
    }

    override suspend fun setHip2(hip: Float) : Unit = withContext(AppDispatchers.io) {
        storageEdit.putFloat(HIP_2, hip).apply()
    }

    override suspend fun setShin(shin: Float) : Unit = withContext(AppDispatchers.io) {
        storageEdit.putFloat(SHIN, shin).apply()
    }

}