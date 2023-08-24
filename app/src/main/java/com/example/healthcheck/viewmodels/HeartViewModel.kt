package com.example.healthcheck.viewmodels

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.models.Heart
import com.example.domain.AppDispatchers
import com.example.healthcheck.util.Constants
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HeartViewModel(application: Application) : AndroidViewModel(application) {

    var settingsForHeart : SharedPreferences

    private val _upperPressure = MutableSharedFlow<Int>()
    val upperPressure = _upperPressure.asSharedFlow()

    private val _lowerPressure = MutableSharedFlow<Int>()
    val lowerPressure = _lowerPressure.asSharedFlow()

    private val _pulse = MutableSharedFlow<Int>()
    val pulse = _pulse.asSharedFlow()

    private val _lastId = MutableSharedFlow<Int>()
    val lastId = _lastId.asSharedFlow()

    private val _lastDate = MutableSharedFlow<Long>()
    val lastDate = _lastDate.asSharedFlow()

    init {
        settingsForHeart = application.applicationContext.getSharedPreferences(Constants.CARDIO, Context.MODE_PRIVATE)

        setCurrentUpperPressure()

        setCurrentLowerPressure()

        setCurrentPulse()

        setCurrentId()

        setCurrentDate()
    }

    suspend fun getCardioFromDataUpPressure(): Int {
        val result = viewModelScope.async(Dispatchers.IO) {
            var list = com.example.data.Repositories.heartRepository.getCardioForDay()
            val sumResult = withContext(Dispatchers.Default) {
                var sum = 0
                for (heart in list) {
                    sum = heart.pressureUp
                }

                return@withContext sum
            }

            return@async sumResult
        }

        return result.await()
    }

    suspend fun getCardioFromDataDownPressure(): Int {
        val result = viewModelScope.async(Dispatchers.IO) {
            var list = com.example.data.Repositories.heartRepository.getCardioForDay()
            val sumResult = withContext(Dispatchers.Default) {
                var sum = 0
                for (heart in list) {
                    sum = heart.pressureDown
                }
                return@withContext sum
            }
            return@async sumResult
        }
        return result.await()
    }

    suspend fun getCardioFromDataPulse(): Int = viewModelScope.async(Dispatchers.Default) {
        val list = withContext(Dispatchers.IO) {
            return@withContext com.example.data.Repositories.heartRepository.getCardioForDay()
        }
        var sum = 0
        for (heart in list) {
            sum = heart.pulse
        }
        return@async sum
    }.await()

    suspend fun getLastIdFromData(): Int = viewModelScope.async(Dispatchers.Default) {
        val list = withContext(Dispatchers.IO) {
            return@withContext com.example.data.Repositories.heartRepository.getCardioForDay()
        }
        var id = 0
        for (heart in list) {
            id = heart.id
        }
        return@async id
    }.await()

    suspend fun getLastDateFromData(): Long = viewModelScope.async(AppDispatchers.default) {
        val list = withContext(AppDispatchers.io) {
            return@withContext com.example.data.Repositories.heartRepository.getCardioForDay()
        }
        var date = 0L
        for (heart in list) {
            date = heart.date
        }

        return@async date
    }.await()

    fun insertHeart(heart : Heart) {
        viewModelScope.launch {
            com.example.data.Repositories.heartRepository.insertCardio(heart)
        }
    }

    fun updateHeart(heart: Heart) {
        viewModelScope.launch {
            com.example.data.Repositories.heartRepository.updateCardio(heart)
        }
    }

    fun setCurrentUpperPressure() {
        viewModelScope.launch(Dispatchers.Main, CoroutineStart.DEFAULT) {
            _upperPressure.emit(getCardioFromDataUpPressure())
        }
    }

    fun setCurrentLowerPressure() {
        viewModelScope.launch(Dispatchers.Main, CoroutineStart.DEFAULT) {
            _lowerPressure.emit(getCardioFromDataDownPressure())
        }
    }

    fun setCurrentPulse() {
        viewModelScope.launch(Dispatchers.Main, CoroutineStart.DEFAULT) {
            _pulse.emit(getCardioFromDataPulse())
        }
    }

    fun setCurrentId() {
        viewModelScope.launch(Dispatchers.Main, CoroutineStart.DEFAULT) {
            _lastId.emit(getLastIdFromData())
        }
    }

    fun setCurrentDate() {
        viewModelScope.launch(Dispatchers.Main, CoroutineStart.DEFAULT) {
            _lastDate.emit(getLastDateFromData())
        }
    }


}