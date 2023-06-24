package com.example.healthcheck.viewmodel

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.healthcheck.model.Repositories
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

class MainFragment1ViewModel(application: Application) : AndroidViewModel(application) {

    lateinit var settings : SharedPreferences
    lateinit var settingsForSleep : SharedPreferences
    lateinit var settingsForWeight : SharedPreferences
    lateinit var settingsForCardio : SharedPreferences

    var daySteps = MutableLiveData<Long?>()
    var dayWeight = MutableLiveData<Long?>()


    private var tripletsPool = ThreadPoolExecutor(3, 3, 5L, TimeUnit.SECONDS, LinkedBlockingQueue())
    init {
        settings = application.applicationContext.getSharedPreferences("targetPref", Context.MODE_PRIVATE)
        settingsForSleep = application.applicationContext.getSharedPreferences("sleep", Context.MODE_PRIVATE)
        settingsForWeight = application.applicationContext.getSharedPreferences("weight", Context.MODE_PRIVATE)
        settingsForCardio = application.applicationContext.getSharedPreferences(Constants.CARDIO, Context.MODE_PRIVATE)

        viewModelScope.launch(Dispatchers.Main, CoroutineStart.DEFAULT) {
            daySteps.value = getLastDateFromData(tripletsPool)
        }
        viewModelScope.launch(Dispatchers.Main, CoroutineStart.DEFAULT) {
            dayWeight.value = getLastDateFromDataWeight(tripletsPool)
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


}