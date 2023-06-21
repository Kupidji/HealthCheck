package com.example.healthcheck.viewmodel

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthcheck.model.Repositories
import com.example.healthcheck.model.steps.entities.Steps
import com.example.healthcheck.model.weight.entities.Weight
import com.example.healthcheck.util.Constants
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

class WeightViewModel(application: Application) : AndroidViewModel(application) {

    lateinit var settingsWeight : SharedPreferences

    var totalWeightForWeek = MutableLiveData<Float?>()
    var totalWeightForMonth = MutableLiveData<Float?>()
    var totalWeightForDay = MutableLiveData<Float>()
    var id = MutableLiveData<Int?>()
    var day = MutableLiveData<Long?>()
    var neck = MutableLiveData<Float?>()
    var waist = MutableLiveData<Float?>()
    var forearm = MutableLiveData<Float?>()
    var wrist = MutableLiveData<Float?>()
    var hips = MutableLiveData<Float?>()
    var hip1 = MutableLiveData<Float?>()
    var hip2 = MutableLiveData<Float?>()
    var shin = MutableLiveData<Float?>()

    private var tripletsPool = ThreadPoolExecutor(3, 3, 5L, TimeUnit.SECONDS, LinkedBlockingQueue())

    init {
        settingsWeight = application.applicationContext.getSharedPreferences("weight", Context.MODE_PRIVATE)
        viewModelScope.launch(Dispatchers.Main, CoroutineStart.DEFAULT) {
            totalWeightForWeek.value = getWeightFromDataForWeek(tripletsPool)
        }

        viewModelScope.launch(Dispatchers.Main, CoroutineStart.DEFAULT) {
            totalWeightForMonth.value = getWeightFromDataForMonth(tripletsPool)
        }

        viewModelScope.launch(Dispatchers.Main, CoroutineStart.DEFAULT) {
            id.value = getLastIdFromDataWeight(tripletsPool)
        }
        viewModelScope.launch(Dispatchers.Main, CoroutineStart.DEFAULT) {
            day.value = getLastDateFromDataWeight(tripletsPool)
        }

        totalWeightForDay.value = settingsWeight.getFloat(Constants.WEIGHT_FOR_DAY, 0F)
        neck.value = settingsWeight.getFloat(Constants.NECK, 0F)
        waist.value = settingsWeight.getFloat(Constants.WAIST, 0F)
        forearm.value = settingsWeight.getFloat(Constants.FOREARM, 0F)
        wrist.value = settingsWeight.getFloat(Constants.WRIST, 0F)
        hips.value = settingsWeight.getFloat(Constants.HIPS, 0F)
        hip1.value = settingsWeight.getFloat(Constants.HIP_1, 0F)
        hip2.value = settingsWeight.getFloat(Constants.HIP_2, 0F)
        shin.value = settingsWeight.getFloat(Constants.SHIN, 0F)
    }

    suspend fun getWeightFromDataForWeek(sheduler : ThreadPoolExecutor) : Float = coroutineScope {
        withContext(sheduler.asCoroutineDispatcher()) {
            var result = async {
                var list = Repositories.weightRepository.getWeightForWeek()
                var sum = 0F
                for (steps in list) {
                    sum += steps
                }
                if (list.isNotEmpty())
                {
                    sum /= list.size
                }
                return@async sum
            }
            result.await()
        }
    }

    suspend fun getWeightFromDataForMonth(sheduler : ThreadPoolExecutor) : Float = coroutineScope {
        withContext(sheduler.asCoroutineDispatcher()) {
            var result = async {
                var list = Repositories.weightRepository.getWeightForMonth()
                var sum = 0F
                for (steps in list) {
                    sum += steps
                }
                if (list.isNotEmpty())
                {
                    sum /= list.size
                }
                return@async sum
            }
            result.await()
        }
    }

    suspend fun getLastDateFromDataWeight(sheduler : ThreadPoolExecutor) : Long = coroutineScope {
        withContext(sheduler.asCoroutineDispatcher()) {
            var result = async {
                var date = 0L
                if (Repositories.weightRepository.getLastWeight() != null) {
                    date = Repositories.weightRepository.getLastWeight().date
                }
                return@async date
            }
            result.await()
        }
    }

    suspend fun getLastIdFromDataWeight(sheduler : ThreadPoolExecutor) : Int = coroutineScope {
        withContext(sheduler.asCoroutineDispatcher()) {
            var result = async {
                var id = 0
                if (Repositories.weightRepository.getLastWeight() != null) {
                    id = Repositories.weightRepository.getLastWeight().id
                }
                return@async id
            }
            result.await()
        }
    }

    fun insertWeight(ourWeight: Weight) {
        viewModelScope.launch {
            Repositories.weightRepository.insertWeight(ourWeight)
        }
    }

    fun updateWeight(ourWeight: Weight) {
        viewModelScope.launch {
            Repositories.weightRepository.updateWeight(ourWeight)
        }
    }

    fun setCurrentWeightForDay(res : Float) {
        totalWeightForDay.value = res
    }

    fun setCurrentWeightForWeek() {
        viewModelScope.launch(Dispatchers.Main, CoroutineStart.DEFAULT) {
            totalWeightForWeek.value = getWeightFromDataForWeek(tripletsPool)
        }
    }

    fun setCurrentWeightForMonth() {
        viewModelScope.launch(Dispatchers.Main, CoroutineStart.DEFAULT) {
            totalWeightForMonth.value = getWeightFromDataForMonth(tripletsPool)
        }
    }

    fun setCurrentIdWeight() {
        viewModelScope.launch(Dispatchers.Main, CoroutineStart.DEFAULT) {
            id.value = getLastIdFromDataWeight(tripletsPool)
        }
    }

    fun setCurrentDateWeight() {
        viewModelScope.launch(Dispatchers.Main, CoroutineStart.DEFAULT) {
            day.value = getLastDateFromDataWeight(tripletsPool)
        }
    }

    fun changeMeasure(humanPart : MutableLiveData<Float?>, costant : String) {
        humanPart.value = settingsWeight.getFloat(costant, 0F)
    }

}