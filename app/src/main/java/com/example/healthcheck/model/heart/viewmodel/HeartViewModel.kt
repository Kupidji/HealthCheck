package com.example.healthcheck.model.heart.viewmodel

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthcheck.model.Repositories
import com.example.healthcheck.model.heart.entities.Heart
import com.example.healthcheck.util.Constants
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.ThreadPoolExecutor

class HeartViewModel(application: Application) : AndroidViewModel(application) {

    var settingsForHeart : SharedPreferences

    var upperPressure = MutableLiveData<Int?>()
    var lowerPressure = MutableLiveData<Int?>()
    var pulse = MutableLiveData<Int?>()
    var lastId = MutableLiveData<Int?>()
    var lastDate = MutableLiveData<Long?>()

    private val tripletsPool = ThreadPoolExecutor(3, 3, 5L, TimeUnit.SECONDS, LinkedBlockingQueue())
    init {
        settingsForHeart = application.applicationContext.getSharedPreferences(Constants.CARDIO, Context.MODE_PRIVATE)

        setCurrentUpperPressure()

        setCurrentLowerPressure()

        setCurrentPulse()

        setCurrentId()

        setCurrentDate()
    }


    suspend fun getCardioFromDataUpPressure(sheduler : ThreadPoolExecutor): Int = coroutineScope {
        withContext(sheduler.asCoroutineDispatcher()) {
            var result = async {
                var list = Repositories.heartRepository.getCardioForDay()
                var sum = 0
                for (heart in list) {
                    sum = heart.pressureUp
                }
                return@async sum
            }
            result.await()
        }
    }

    suspend fun getCardioFromDataDownPressure(sheduler : ThreadPoolExecutor): Int = coroutineScope {
        withContext(sheduler.asCoroutineDispatcher()) {
            var result = async {
                var list = Repositories.heartRepository.getCardioForDay()
                var sum = 0
                for (heart in list) {
                    sum = heart.pressureDown
                }
                return@async sum
            }
            result.await()
        }
    }

    suspend fun getCardioFromDataPulse(sheduler : ThreadPoolExecutor): Int = coroutineScope {
        withContext(sheduler.asCoroutineDispatcher()) {
            var result = async {
                var list = Repositories.heartRepository.getCardioForDay()
                var sum = 0
                for (heart in list) {
                    sum = heart.pulse
                }
                return@async sum
            }
            result.await()
        }
    }

    suspend fun getLastIdFromData(sheduler : ThreadPoolExecutor): Int = coroutineScope {
        withContext(sheduler.asCoroutineDispatcher()) {
            var result = async {
                var list = Repositories.heartRepository.getCardioForDay()
                var id = 0
                for (heart in list) {
                    id = heart.id
                }
                return@async id
            }
            result.await()
        }
    }

    suspend fun getLastDateFromData(sheduler : ThreadPoolExecutor): Long = coroutineScope {
        withContext(sheduler.asCoroutineDispatcher()) {
            var result = async {
                var list = Repositories.heartRepository.getCardioForDay()
                var date = 0L
                for (heart in list) {
                    date = heart.date
                }
                return@async date
            }
            result.await()
        }
    }

    fun insertHeart(heart : Heart) {
        viewModelScope.launch {
            Repositories.heartRepository.insertCardio(heart)
        }
    }

    fun updateHeart(heart: Heart) {
        viewModelScope.launch {
            Repositories.heartRepository.updateCardio(heart)
        }
    }

    fun setCurrentUpperPressure() {
        viewModelScope.launch(Dispatchers.Main, CoroutineStart.DEFAULT) {
            upperPressure.value = getCardioFromDataUpPressure(tripletsPool)
        }
    }

    fun setCurrentLowerPressure() {
        viewModelScope.launch(Dispatchers.Main, CoroutineStart.DEFAULT) {
            lowerPressure.value = getCardioFromDataDownPressure(tripletsPool)
        }
    }

    fun setCurrentPulse() {
        viewModelScope.launch(Dispatchers.Main, CoroutineStart.DEFAULT) {
            pulse.value = getCardioFromDataPulse(tripletsPool)
        }
    }

    fun setCurrentId() {
        viewModelScope.launch(Dispatchers.Main, CoroutineStart.DEFAULT) {
            lastId.value = getLastIdFromData(tripletsPool)
        }
    }

    fun setCurrentDate() {
        viewModelScope.launch(Dispatchers.Main, CoroutineStart.DEFAULT) {
            lastDate.value = getLastDateFromData(tripletsPool)
        }
    }


}