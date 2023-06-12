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
import com.example.healthcheck.util.Constants
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

class StepsViewModel(application: Application) : AndroidViewModel(application)  {

    lateinit var settings : SharedPreferences

    var totalStepsForWeek = MutableLiveData<Int?>()
    var totalStepsForMonth = MutableLiveData<Int?>()
    var totalStepsForDay = MutableLiveData<Int>()

    init {
        settings = application.applicationContext.getSharedPreferences("targetPref", Context.MODE_PRIVATE)
        val tripletsPool = ThreadPoolExecutor(3, 3, 5L, TimeUnit.SECONDS, LinkedBlockingQueue())
        viewModelScope.launch(Dispatchers.Main, CoroutineStart.DEFAULT) {
            totalStepsForWeek.value = getStepsFromDataForWeek(tripletsPool)
        }
        viewModelScope.launch(Dispatchers.Main, CoroutineStart.DEFAULT) {
            totalStepsForMonth.value = getStepsFromDataForMonth(tripletsPool)
        }
        totalStepsForDay.value = settings.getInt(Constants.STEPS_PER_DAY, 0)
    }

//    fun setTotalStepsForDay(res : Int) {
//        totalStepsForDay.value = res
//    }

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

    fun insertSteps(ourSteps : Steps) {
        viewModelScope.launch {
            Repositories.stepsRepository.insertCountOfSteps(ourSteps)
        }
    }


}