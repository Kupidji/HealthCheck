package com.example.healthcheck.viewmodels.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.Repositories
import com.example.domain.AppDispatchers
import com.example.domain.usecase.GetAverageOfSleepForWeek
import com.example.domain.usecase.GetHeartForWeekFromDb
import com.example.domain.usecase.GetWeightForWeekFromDb
import com.example.domain.usecase.steps.GetAverageOfStepsForWeekFromDb
import com.example.domain.usecase.steps.GetStepsTarget
import com.example.domain.usecase.weight.GetWeightTarget
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class MainFragment2ViewModel(application: Application) : AndroidViewModel(application) {

    private val _totalStepsForWeek = MutableSharedFlow<Int>(1, 0, BufferOverflow.DROP_OLDEST)
    val totalStepsForWeek = _totalStepsForWeek.asSharedFlow()

    private val _averageSleep = MutableSharedFlow<String>(1, 0, BufferOverflow.DROP_OLDEST)
    val averageSleepWeek = _averageSleep.asSharedFlow()

    private val _totalWeightForWeek = MutableSharedFlow<Float>(1, 0, BufferOverflow.DROP_OLDEST)
    val totalWeightForWeek = _totalWeightForWeek.asSharedFlow()

    private val _weekHeart = MutableSharedFlow<String>(1, 0, BufferOverflow.DROP_OLDEST)
    val weekHeart = _weekHeart.asSharedFlow()

    private val _stepsTarget = MutableSharedFlow<Int>(1, 0, BufferOverflow.DROP_OLDEST)
    val stepsTarget = _stepsTarget.asSharedFlow()

    private val _weightTarget = MutableSharedFlow<Float>(1, 0, BufferOverflow.DROP_OLDEST)
    val weightTarget = _weightTarget.asSharedFlow()


    init {
        viewModelScope.launch {
            val getAverageOfStepsForWeekFromDb = GetAverageOfStepsForWeekFromDb(repository = Repositories.stepsRepository)
            getAverageOfStepsForWeekFromDb.execute().collect { average ->
                _totalStepsForWeek.emit(average)
            }
        }

        viewModelScope.launch {
            val getAverageOfSleepForWeek = GetAverageOfSleepForWeek(repository = Repositories.sleepRepository)
            getAverageOfSleepForWeek.execute().collect { time ->
                _averageSleep.emit(time)
            }
        }

        viewModelScope.launch {
            val getWeightForWeekFromDb = GetWeightForWeekFromDb(repository = Repositories.weightRepository)
            getWeightForWeekFromDb.execute().collect { weight ->
                _totalWeightForWeek.emit(weight)
            }
        }

        viewModelScope.launch {
            val getHeartForWeekFromDb = GetHeartForWeekFromDb(repository = Repositories.heartRepository)
            getHeartForWeekFromDb.execute().collect { heart ->
                _weekHeart.emit(heart)
            }
        }

        viewModelScope.launch(AppDispatchers.main) {
            val getStepsTarget = GetStepsTarget(repository = Repositories.stepsStorage)
            _stepsTarget.emit(getStepsTarget.execute())
        }

        viewModelScope.launch(AppDispatchers.main) {
            val getWeightTarget = GetWeightTarget(repository = Repositories.weightStorage)
            _weightTarget.emit(getWeightTarget.execute())
        }

    }

}