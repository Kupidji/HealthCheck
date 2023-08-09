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
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class StepsViewModel(application: Application) : AndroidViewModel(application)  {

    var settings : SharedPreferences
    var settingsWeight : SharedPreferences

    private val _totalStepsForDay = MutableSharedFlow<Int>(1, 0, BufferOverflow.DROP_OLDEST)
    val totalStepsForDay = _totalStepsForDay.asSharedFlow()

    private val _totalStepsForWeek = MutableSharedFlow<Int>(1, 0, BufferOverflow.DROP_OLDEST)
    val totalStepsForWeek = _totalStepsForWeek.asSharedFlow()

    private val _totalStepsForMonth = MutableSharedFlow<Int>(1, 0, BufferOverflow.DROP_OLDEST)
    val totalStepsForMonth = _totalStepsForMonth.asSharedFlow()

    private val _currentGoal = MutableSharedFlow<Int>(1, 0, BufferOverflow.DROP_OLDEST)
    val currentGoal = _currentGoal.asSharedFlow()

    private val _id = MutableSharedFlow<Int>(1, 0, BufferOverflow.DROP_OLDEST)
    val id = _id.asSharedFlow()

    private val _day = MutableSharedFlow<Long>(1, 0, BufferOverflow.DROP_OLDEST)
    val day = _day.asSharedFlow()

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

    suspend fun getStepsFromDataForWeek() : Int {
        val list = Repositories.stepsRepository.getStepsForWeek()
        var result = 0

        for (steps in list) {
            result += steps
        }

        return result
    }

    suspend fun getStepsFromDataForMonth() : Int {
        val list = Repositories.stepsRepository.getStepsForMonth()
        var result = 0

        for (steps in list) {
            result += steps
        }

        return result
    }

    suspend fun getLastDateFromData() : Long {
        var result = 0L
        if (Repositories.stepsRepository.getLastDate() != null) {
            result = Repositories.stepsRepository.getLastDate().date
        }

        return result
    }

    suspend fun getLastIdFromData() : Int {
        var result = 0
        if (Repositories.stepsRepository.getLastDate() != null) {
            result = Repositories.stepsRepository.getLastDate().id
        }

        return result
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
        viewModelScope.launch {
            _currentGoal.emit(res)
        }
    }

    fun setCurrentStepsForDay(res : Int) {
        viewModelScope.launch {
            _totalStepsForDay.emit(res)
        }

    }

    fun setCurrentStepsForWeek() {
        viewModelScope.launch(Dispatchers.Main, CoroutineStart.DEFAULT) {
            _totalStepsForWeek.emit(getStepsFromDataForWeek())
        }
    }

    fun setCurrentStepsForMonth() {
        viewModelScope.launch(Dispatchers.Main, CoroutineStart.DEFAULT) {
            _totalStepsForMonth.emit(getStepsFromDataForMonth())
        }
    }

    fun setCurrentId() {
        viewModelScope.launch(Dispatchers.Main, CoroutineStart.DEFAULT) {
            _id.emit(getLastIdFromData())
        }
    }

    fun setCurrentDate() {
        viewModelScope.launch(Dispatchers.Main, CoroutineStart.DEFAULT) {
            _day.emit(getLastDateFromData())
        }
    }

}