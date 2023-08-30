package com.example.healthcheck.viewmodels.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.Repositories
import com.example.domain.AppDispatchers
import com.example.domain.usecase.GetHeartForDayFromDb
import com.example.domain.usecase.GetHoursOfSleepForDayFromDb
import com.example.domain.usecase.GetWeightForDayFromDb
import com.example.domain.usecase.heart.GetLastHeartIdAndDate
import com.example.domain.usecase.sleep.GetLastSleepIdAndDate
import com.example.domain.usecase.steps.GetCountOfStepsForDayFromDb
import com.example.domain.usecase.steps.GetLastStepsIdAndDate
import com.example.domain.usecase.steps.GetStepsTarget
import com.example.domain.usecase.weight.GetLastWeightIdAndDate
import com.example.domain.usecase.weight.GetWeightTarget
import com.example.healthcheck.App
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar

class MainFragment1ViewModel : ViewModel() {

    private val _daySteps = MutableSharedFlow<Int>(1, 0, BufferOverflow.DROP_OLDEST)
    val daySteps = _daySteps.asSharedFlow()

    private val _daySleep = MutableSharedFlow<String>(1, 0, BufferOverflow.DROP_OLDEST)
    val daySleep = _daySleep.asSharedFlow()

    private val _dayWeight = MutableSharedFlow<Float>(1, 0, BufferOverflow.DROP_OLDEST)
    val dayWeight = _dayWeight.asSharedFlow()

    private val _dayHeart = MutableSharedFlow<String>(1, 0, BufferOverflow.DROP_OLDEST)
    val dayHeart = _dayHeart.asSharedFlow()

    private val _stepsTarget = MutableSharedFlow<Int>(1, 0, BufferOverflow.DROP_OLDEST)
    val stepsTarget = _stepsTarget.asSharedFlow()

    private val _weightTarget = MutableSharedFlow<Float>(1, 0, BufferOverflow.DROP_OLDEST)
    val weightTarget = _weightTarget.asSharedFlow()

    init {
        viewModelScope.launch(AppDispatchers.main) {
            val getLastStepsIdAndDate = GetLastStepsIdAndDate(repository = Repositories.stepsRepository)
            getLastStepsIdAndDate.execute().collect { date ->
                val currentDate = SimpleDateFormat("dd.MM").format(Calendar.getInstance().timeInMillis)
                val lastDate = SimpleDateFormat("dd.MM").format(date.date)

                if (lastDate == currentDate) {
                    val getCountOfStepsForDayFromDb = GetCountOfStepsForDayFromDb(repository = Repositories.stepsRepository)
                    getCountOfStepsForDayFromDb.execute().collect { countOfSteps ->
                        _daySteps.emit(countOfSteps)
                    }
                }
                else {
                    _daySteps.emit(0)
                }
            }
        }

        viewModelScope.launch(AppDispatchers.main) {
            val getLastSleepIdAndDate = GetLastSleepIdAndDate(repository = Repositories.sleepRepository)
            getLastSleepIdAndDate.execute().collect { date ->
                val currentDate = SimpleDateFormat("dd.MM").format(Calendar.getInstance().timeInMillis)
                val lastDate = SimpleDateFormat("dd.MM").format(date.date)

                if (lastDate != currentDate) {
                    _daySleep.emit("")
                }
                else {
                    val getHoursOfSleepForDayFromDb = GetHoursOfSleepForDayFromDb(repository = Repositories.sleepRepository)
                    getHoursOfSleepForDayFromDb.execute().collect { time ->
                        _daySleep.emit(time)
                    }
                }
            }
        }

        viewModelScope.launch(AppDispatchers.main) {
            val getLastWeightIdAndDate = GetLastWeightIdAndDate(repository = Repositories.weightRepository)
            getLastWeightIdAndDate.execute().collect { date ->
                val currentDate = SimpleDateFormat("dd.MM").format(Calendar.getInstance().timeInMillis)
                val lastDate = SimpleDateFormat("dd.MM").format(date.date)
                if (lastDate == currentDate) {
                    val getWeightForDayFromDb = GetWeightForDayFromDb(repository = Repositories.weightRepository)
                    getWeightForDayFromDb.execute().collect { weight ->
                        _dayWeight.emit(weight)
                    }
                }
                else {
                    _dayWeight.emit(0F)
                }
            }
        }

        viewModelScope.launch(AppDispatchers.main) {
            val getLastHeartIdAndDate = GetLastHeartIdAndDate(repository = Repositories.heartRepository)
            getLastHeartIdAndDate.execute().collect { date ->
                val lastDate = SimpleDateFormat("dd.MM").format(date.date)
                val currentDate = SimpleDateFormat("dd.MM").format(Calendar.getInstance().timeInMillis)
                if (lastDate != currentDate) {
                    _dayHeart.emit("")
                }
                else {
                    val getHeartForDayFromDb = GetHeartForDayFromDb(repository = Repositories.heartRepository)
                    getHeartForDayFromDb.execute().collect { heart ->
                        _dayHeart.emit(heart)
                    }
                }
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

    fun updateStepsTarget() {
        viewModelScope.launch(AppDispatchers.main) {
            val getStepsTarget = GetStepsTarget(repository = Repositories.stepsStorage)
            _stepsTarget.emit(getStepsTarget.execute())
        }
    }

}