package com.example.healthcheck.viewmodel

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.healthcheck.model.Repositories
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

class MainFragment1ViewModel(application: Application) : AndroidViewModel(application) {

    lateinit var settings : SharedPreferences
    lateinit var settingsForSleep : SharedPreferences
    lateinit var settingsForWeight : SharedPreferences
    lateinit var settingsForCardio : SharedPreferences

    var daySteps = MutableLiveData<Long?>()
    var dayWeight = MutableLiveData<Long?>()
//    var steps = MutableLiveData<Int?>()
//    var weight = MutableLiveData<Float?>()

    private var tripletsPool = ThreadPoolExecutor(3, 3, 5L, TimeUnit.SECONDS, LinkedBlockingQueue())
    init {
        settings = application.applicationContext.getSharedPreferences("targetPref", Context.MODE_PRIVATE)
        settingsForSleep = application.applicationContext.getSharedPreferences("sleep", Context.MODE_PRIVATE)
        settingsForWeight = application.applicationContext.getSharedPreferences("weight", Context.MODE_PRIVATE)
        settingsForCardio = application.applicationContext.getSharedPreferences("cardio", Context.MODE_PRIVATE)

        viewModelScope.launch(Dispatchers.Main, CoroutineStart.DEFAULT) {
            daySteps.value = getLastDateFromData(tripletsPool)
        }
        viewModelScope.launch(Dispatchers.Main, CoroutineStart.DEFAULT) {
            dayWeight.value = getLastDateFromDataWeight(tripletsPool)
        }
//        viewModelScope.launch(Dispatchers.Main, CoroutineStart.DEFAULT) {
//            steps.value = getLastStepsFromData(tripletsPool)
//        }
//        viewModelScope.launch(Dispatchers.Main, CoroutineStart.DEFAULT) {
//            weight.value = getLastWeightFromDataWeight(tripletsPool)
//        }
    }

    suspend fun getLastDateFromData(sheduler : ThreadPoolExecutor) : Long = coroutineScope {
        withContext(sheduler.asCoroutineDispatcher()) {
            var result = async {
                var date = 0L
                if (Repositories.stepsRepository.getLastDate() != null) {
                    date = Repositories.stepsRepository.getLastDate().date
                }
                return@async date
            }
            result.await()
        }
    }

//    suspend fun getLastStepsFromData(sheduler : ThreadPoolExecutor) : Int = coroutineScope {
//        withContext(sheduler.asCoroutineDispatcher()) {
//            var result = async {
//                var steps = 0
//                if (Repositories.stepsRepository.getLastDate() != null) {
//                    steps = Repositories.stepsRepository.getLastDate().countOfSteps
//                }
//                return@async steps
//            }
//            result.await()
//        }
//    }

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

//    suspend fun getLastWeightFromDataWeight(sheduler : ThreadPoolExecutor) : Float = coroutineScope {
//        withContext(sheduler.asCoroutineDispatcher()) {
//            var result = async {
//                var weight = 0F
//                if (Repositories.weightRepository.getLastWeight() != null) {
//                    weight = Repositories.weightRepository.getLastWeight().weight
//                }
//                return@async weight
//            }
//            result.await()
//        }
//    }

}