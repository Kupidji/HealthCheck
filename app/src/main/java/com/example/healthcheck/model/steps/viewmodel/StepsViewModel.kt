package com.example.healthcheck.model.steps.viewmodel

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.healthcheck.model.Repositories
import com.example.healthcheck.model.steps.entities.Steps
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

class StepsViewModel(application: Application) : AndroidViewModel(application)  {

    var settings : SharedPreferences
    var settingsWeight : SharedPreferences

    var totalStepsForWeek = MutableLiveData<Int?>()
    var totalStepsForMonth = MutableLiveData<Int?>()
    var totalStepsForDay = MutableLiveData<Int>()
    var currentGoal = MutableLiveData<Int>()
    var id = MutableLiveData<Int?>()
    var day = MutableLiveData<Long?>()

    private var tripletsPool = ThreadPoolExecutor(3, 3, 5L, TimeUnit.SECONDS, LinkedBlockingQueue())

    init {
        settings = application.applicationContext.getSharedPreferences(Constants.STEPS, Context.MODE_PRIVATE)
        settingsWeight = application.applicationContext.getSharedPreferences(Constants.WEIGHT, Context.MODE_PRIVATE)

        setCurrentStepsForWeek()
        setCurrentStepsForMonth()
        setCurrentId()
        setCurrentDate()
        setCurrentStepsForDay(settings.getInt(Constants.STEPS_PER_DAY, 0))
        setCurrentTarget(settings.getInt(Constants.TARGET, 10000))

    }

    suspend fun getStepsFromDataForWeek(sheduler : ThreadPoolExecutor) : Int = coroutineScope {
        withContext(sheduler.asCoroutineDispatcher()) {
            var result = async {
                var list = Repositories.stepsRepository.getStepsForWeek()
                var sum = 0
                for (steps in list) {
                    sum += steps
                }
                return@async sum
            }

            result.await()
        }
    }

    suspend fun getStepsFromDataForMonth(sheduler : ThreadPoolExecutor) : Int = coroutineScope {
        withContext(sheduler.asCoroutineDispatcher()) {
            var result = async {
                var list = Repositories.stepsRepository.getStepsForMonth()
                var sum = 0
                for (steps in list) {
                    sum += steps
                }
                return@async sum
            }

            result.await()
        }
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

     suspend fun getLastIdFromData(sheduler : ThreadPoolExecutor) : Int = coroutineScope {
         withContext(sheduler.asCoroutineDispatcher()) {
             var result = async {
                 var id = 0
                 if (Repositories.stepsRepository.getLastDate() != null) {
                     id = Repositories.stepsRepository.getLastDate().id
                 }
                 return@async id
             }
             result.await()
         }
    }

    fun insertSteps(ourSteps : Steps) {
        viewModelScope.launch {
            Repositories.stepsRepository.insertCountOfSteps(ourSteps)
        }
    }

    fun updateSteps(ourSteps: Steps) {
        viewModelScope.launch {
            Repositories.stepsRepository.updateCountOfSteps(ourSteps)
        }
    }

    fun setCurrentTarget(res : Int) {
        currentGoal.value = res
    }

    fun setCurrentStepsForDay(res : Int) {
        totalStepsForDay.value = res
    }

    fun setCurrentStepsForWeek() {
        viewModelScope.launch(Dispatchers.Main, CoroutineStart.DEFAULT) {
            totalStepsForWeek.value = getStepsFromDataForWeek(tripletsPool)
        }
    }

    fun setCurrentStepsForMonth() {
        viewModelScope.launch(Dispatchers.Main, CoroutineStart.DEFAULT) {
            totalStepsForMonth.value = getStepsFromDataForMonth(tripletsPool)
        }
    }

    fun setCurrentId() {
        viewModelScope.launch(Dispatchers.Main, CoroutineStart.DEFAULT) {
            id.value = getLastIdFromData(tripletsPool)
        }
    }

    fun setCurrentDate() {
        viewModelScope.launch(Dispatchers.Main, CoroutineStart.DEFAULT) {
            day.value = getLastDateFromData(tripletsPool)
        }
    }

}