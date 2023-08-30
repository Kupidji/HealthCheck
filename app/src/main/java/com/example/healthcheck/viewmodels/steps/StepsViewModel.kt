package com.example.healthcheck.viewmodels.steps

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.Repositories
import com.example.domain.AppDispatchers
import com.example.domain.models.Steps
import com.example.domain.usecase.steps.GetCountOfStepsForDayFromDb
import com.example.domain.usecase.steps.GetCountOfStepsForMonthFromDb
import com.example.domain.usecase.steps.GetCountOfStepsForWeekFromDb
import com.example.domain.usecase.steps.GetKkal
import com.example.domain.usecase.steps.GetLastStepsIdAndDate
import com.example.domain.usecase.steps.GetStepsTarget
import com.example.domain.usecase.steps.InsertSteps
import com.example.domain.usecase.steps.SetStepsTarget
import com.example.domain.usecase.steps.UpdateSteps
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar

class StepsViewModel : ViewModel() {

    private val _totalStepsForDay = MutableSharedFlow<Int>(1, 0, BufferOverflow.DROP_OLDEST)
    val totalStepsForDay = _totalStepsForDay.asSharedFlow()

    private val _totalStepsForWeek = MutableSharedFlow<Int>(1, 0, BufferOverflow.DROP_OLDEST)
    val totalStepsForWeek = _totalStepsForWeek.asSharedFlow()

    private val _totalStepsForMonth = MutableSharedFlow<Int>(1, 0, BufferOverflow.DROP_OLDEST)
    val totalStepsForMonth = _totalStepsForMonth.asSharedFlow()

    private val _currentTarget = MutableSharedFlow<Int>(1, 0, BufferOverflow.DROP_OLDEST)
    val currentTarget = _currentTarget.asSharedFlow()

    private val _id = MutableSharedFlow<Int>(1, 0, BufferOverflow.DROP_OLDEST)
    val id = _id.asSharedFlow()

    private val _day = MutableSharedFlow<Long>(1, 0, BufferOverflow.DROP_OLDEST)
    val day = _day.asSharedFlow()

    private val _kkalForDay = MutableSharedFlow<Int>(1, 0, BufferOverflow.DROP_OLDEST)
    val kkalForDay = _kkalForDay.asSharedFlow()

    private val _kkalForWeek = MutableSharedFlow<Int>(1, 0, BufferOverflow.DROP_OLDEST)
    val kkalForWeek = _kkalForWeek.asSharedFlow()

    private val _kkalForMonth = MutableSharedFlow<Int>(1, 0, BufferOverflow.DROP_OLDEST)
    val kkalForMonth = _kkalForMonth.asSharedFlow()

    init {
        //инициализация шагов за сегодня. Если дата последней записи совпадает с сегодняшней датой, то выводится последняя
        //запись, иначе заглушка
        viewModelScope.launch {
            val getLastStepsIdAndDate = GetLastStepsIdAndDate(repository = Repositories.stepsRepository)
            getLastStepsIdAndDate.execute().collect { stepsEntity ->
                val currentDate = SimpleDateFormat("dd.MM").format(Calendar.getInstance().timeInMillis)
                val lastDate = SimpleDateFormat("dd.MM").format(stepsEntity.date)
                if (lastDate == currentDate) {
                    val getCountOfStepsForDayFromDb = GetCountOfStepsForDayFromDb(repository = Repositories.stepsRepository)
                    getCountOfStepsForDayFromDb.execute().collect { countOfSteps ->
                        _totalStepsForDay.emit(countOfSteps)
                    }
                }
                else {
                    _totalStepsForDay.emit(0)
                }
            }

        }

        viewModelScope.launch {
            val getCountOfStepsForWeekFromDb = GetCountOfStepsForWeekFromDb(repository = Repositories.stepsRepository)
            getCountOfStepsForWeekFromDb.execute().collect { countOfSteps ->
                _totalStepsForWeek.emit(countOfSteps)
            }
        }

        viewModelScope.launch {
            val getCountOfStepsForMonthFromDb = GetCountOfStepsForMonthFromDb(repository = Repositories.stepsRepository)
            getCountOfStepsForMonthFromDb.execute().collect { countOfSteps ->
                _totalStepsForMonth.emit(countOfSteps)
            }
        }

        viewModelScope.launch {
            val getStepsTarget = GetStepsTarget(repository = Repositories.stepsStorage)
            _currentTarget.emit(getStepsTarget.execute())
        }

        viewModelScope.launch {
            val getLastStepsIdAndDate = GetLastStepsIdAndDate(repository = Repositories.stepsRepository)
            getLastStepsIdAndDate.execute().collect { stepsEntity ->
                _id.emit(stepsEntity.id)
            }

        }

        viewModelScope.launch {
            val getLastStepsIdAndDate = GetLastStepsIdAndDate(repository = Repositories.stepsRepository)
            getLastStepsIdAndDate.execute().collect { stepsEntity ->
                _day.emit(stepsEntity.date)
            }
        }

    }


    fun insertSteps(steps : Int) {
        viewModelScope.launch {
            var currentDate = Calendar.getInstance().timeInMillis
            val insetSteps = InsertSteps(Repositories.stepsRepository)
            insetSteps.execute(
                Steps(
                    id = 0,
                    countOfSteps = steps,
                    date = currentDate,
                )
            )
//            _id.emit(getLastIdFromData())
//            _day.emit(getLastDateFromData())
           // _totalStepsForDay.emit(steps)

//            val getCountOfStepsForWeekFromDb = GetCountOfStepsForWeekFromDb(repository = Repositories.stepsRepository)
//            _totalStepsForWeek.emit(getCountOfStepsForWeekFromDb.execute())

//            val getCountOfStepsForMonthFromDb = GetCountOfStepsForMonthFromDb(repository = Repositories.stepsRepository)
//            _totalStepsForMonth.emit(getCountOfStepsForMonthFromDb.execute())

        }
    }

    fun updateSteps(id : Int, steps : Int) {
        viewModelScope.launch {
            var currentDate = Calendar.getInstance().timeInMillis

            val updateSteps = UpdateSteps(repository = Repositories.stepsRepository)
            updateSteps.execute(steps = Steps(
                    id = id,
                    countOfSteps = steps,
                    date = currentDate,
                )
            )
           // _totalStepsForDay.emit(steps)

//            val getCountOfStepsForWeekFromDb = GetCountOfStepsForWeekFromDb(repository = Repositories.stepsRepository)
//            _totalStepsForWeek.emit(getCountOfStepsForWeekFromDb.execute())

//            val getCountOfStepsForMonthFromDb = GetCountOfStepsForMonthFromDb(repository = Repositories.stepsRepository)
//            _totalStepsForMonth.emit(getCountOfStepsForMonthFromDb.execute())

        }
    }

    fun setTarget(target : Int) {
        viewModelScope.launch {
            val setStepsTarget = SetStepsTarget(repository = Repositories.stepsStorage)
            setStepsTarget.execute(target)
            _currentTarget.emit(target)
        }
    }

    fun getCurrentTarget() {
        viewModelScope.launch {
            val getStepsTarget = GetStepsTarget(repository = Repositories.stepsStorage)
            _currentTarget.emit(getStepsTarget.execute())
        }
    }

    fun getKkalForDay(steps : Int)  {
        viewModelScope.launch(AppDispatchers.main) {
            val getKkal = GetKkal(repository = Repositories.weightRepository)
            getKkal.execute(steps = steps).collect { kkal ->
                _kkalForDay.emit(kkal)
            }
        }
    }

    fun getKkalForWeek(steps : Int) {
        viewModelScope.launch(AppDispatchers.main) {
            val getKkal = GetKkal(repository = Repositories.weightRepository)
            getKkal.execute(steps = steps).collect { kkal ->
                _kkalForWeek.emit(kkal)
            }
        }
    }

    fun getKkalForMonth(steps : Int) {
        viewModelScope.launch(AppDispatchers.main) {
            val getKkal = GetKkal(repository = Repositories.weightRepository)
            getKkal.execute(steps = steps).collect { kkal ->
                _kkalForMonth.emit(kkal)
            }
        }
    }

}