package com.example.healthcheck.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.Repositories
import com.example.domain.AppDispatchers
import com.example.domain.models.Sleep
import com.example.domain.usecase.GetAverageOfSleepForMonth
import com.example.domain.usecase.GetAverageOfSleepForWeek
import com.example.domain.usecase.sleep.GetGoToSleepTime
import com.example.domain.usecase.sleep.GetHoursOfSleepForMonthFromDb
import com.example.domain.usecase.sleep.GetHoursOfSleepForWeekFromDb
import com.example.domain.usecase.sleep.GetWakeUpTime
import com.example.domain.usecase.sleep.InsertSleep
import com.example.domain.usecase.sleep.SetGoToSleepTime
import com.example.domain.usecase.sleep.SetWakeUpTime
import com.example.domain.usecase.sleep.UpdateSleep
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Math.abs
import java.util.Calendar

class SleepViewModel : ViewModel() {

    private val _id = MutableSharedFlow<Int>(1, 0, BufferOverflow.DROP_OLDEST)
    val id = _id.asSharedFlow()

    private val _date = MutableSharedFlow<Long>(1, 0, BufferOverflow.DROP_OLDEST)
    val date = _date.asSharedFlow()

    private val _sleepForDay = MutableSharedFlow<String>(1, 0, BufferOverflow.DROP_OLDEST)
    val sleepForDay = _sleepForDay.asSharedFlow()

    private val _sleepForWeek = MutableSharedFlow<String>(1, 0, BufferOverflow.DROP_OLDEST)
    val sleepForWeek = _sleepForWeek.asSharedFlow()

    private val _sleepForMonth = MutableSharedFlow<String>(1, 0, BufferOverflow.DROP_OLDEST)
    val sleepForMonth = _sleepForMonth.asSharedFlow()

    private val _averageSleepForWeek = MutableSharedFlow<String>(1, 0, BufferOverflow.DROP_OLDEST)
    val averageSleepForWeek = _averageSleepForWeek.asSharedFlow()

    private val _averageSleepForMonth = MutableSharedFlow<String>(1, 0, BufferOverflow.DROP_OLDEST)
    val averageSleepForMonth = _averageSleepForMonth.asSharedFlow()

    init {
        viewModelScope.launch(AppDispatchers.main) {
            _id.emit(getLastIdFromData())
        }

        viewModelScope.launch(AppDispatchers.main) {
            _date.emit(getLastDateFromData())
        }

        viewModelScope.launch(AppDispatchers.main) {
            val getGoToSleepTime = GetGoToSleepTime(repository = Repositories.sleepStorage)
            val goToSleepTime = getGoToSleepTime.execute()
            val getWakeUpTime = GetWakeUpTime(repository = Repositories.sleepStorage)
            val wakeUpTime = getWakeUpTime.execute()

            if ((goToSleepTime != 0L && wakeUpTime != 0L) && (goToSleepTime != wakeUpTime)) {
                _sleepForDay.emit(showDiffBetweenTimes(time1 = goToSleepTime, time2 = wakeUpTime))
            }
            else {
                _sleepForDay.emit("")
            }
        }

        viewModelScope.launch(AppDispatchers.main) {
            val getHoursOfSleepForWeekFromDb = GetHoursOfSleepForWeekFromDb(repository = Repositories.sleepRepository)
            _sleepForWeek.emit(getHoursOfSleepForWeekFromDb.execute())
        }

        viewModelScope.launch(AppDispatchers.main) {
            val getHoursOfSleepForMonthFromDb = GetHoursOfSleepForMonthFromDb(repository = Repositories.sleepRepository)
            _sleepForMonth.emit(getHoursOfSleepForMonthFromDb.execute())
        }

        viewModelScope.launch(AppDispatchers.main) {
            val getAverageOfSleepForWeek = GetAverageOfSleepForWeek(repository = Repositories.sleepRepository)
            _averageSleepForWeek.emit(getAverageOfSleepForWeek.execute())
        }

        viewModelScope.launch(AppDispatchers.main) {
            val getAverageOfSleepForMonth = GetAverageOfSleepForMonth(repository = Repositories.sleepRepository)
            _averageSleepForMonth.emit(getAverageOfSleepForMonth.execute())
        }

    }

    suspend fun getLastIdFromData() : Int = viewModelScope.async(AppDispatchers.default) {
        try {
            val sleep = withContext(AppDispatchers.io) {
                return@withContext Repositories.sleepRepository.getLastDate()
            }
            var lastId = sleep.id
            return@async lastId
        }
        catch (e : NullPointerException) {
            return@async 0
        }
    }.await()

    suspend fun getLastDateFromData() : Long = viewModelScope.async(AppDispatchers.default) {
        try {
            val sleep = withContext(AppDispatchers.io) {
                return@withContext Repositories.sleepRepository.getLastDate()
            }
            var lastDate = sleep.date

            return@async lastDate
        }
        catch (e : NullPointerException) {
            return@async 0L
        }
    }.await()

    fun insertSleep(goToSleepTime : Long, wakeUpTime : Long) {
        viewModelScope.launch {
            val insertSleep = InsertSleep(repository = Repositories.sleepRepository)
            val currentDate = Calendar.getInstance().timeInMillis

            insertSleep.execute(
                sleep = Sleep (
                    id = 0,
                    timeOfSleep = calculateTimeBetweenStartEndSleep(goToSleepTime, wakeUpTime),
                    date = currentDate
                )
            )
            val getHoursOfSleepForWeekFromDb = GetHoursOfSleepForWeekFromDb(repository = Repositories.sleepRepository)
            _sleepForWeek.emit(getHoursOfSleepForWeekFromDb.execute())

            val getHoursOfSleepForMonthFromDb = GetHoursOfSleepForMonthFromDb(repository = Repositories.sleepRepository)
            _sleepForMonth.emit(getHoursOfSleepForMonthFromDb.execute())

            val getAverageOfSleepForWeek = GetAverageOfSleepForWeek(repository = Repositories.sleepRepository)
            _averageSleepForWeek.emit(getAverageOfSleepForWeek.execute())

            val getAverageOfSleepForMonth = GetAverageOfSleepForMonth(repository = Repositories.sleepRepository)
            _averageSleepForMonth.emit(getAverageOfSleepForMonth.execute())
        }
    }

    fun updateSleep(id : Int, goToSleepTime : Long, wakeUpTime : Long) {
        viewModelScope.launch {
            val updateSleep = UpdateSleep(repository = Repositories.sleepRepository)
            val currentDate = Calendar.getInstance().timeInMillis

            updateSleep.execute(
                sleep = Sleep(
                    id = id,
                    timeOfSleep = calculateTimeBetweenStartEndSleep(goToSleepTime, wakeUpTime),
                    date = currentDate
                )
            )
            val getHoursOfSleepForWeekFromDb = GetHoursOfSleepForWeekFromDb(repository = Repositories.sleepRepository)
            _sleepForWeek.emit(getHoursOfSleepForWeekFromDb.execute())

            val getHoursOfSleepForMonthFromDb = GetHoursOfSleepForMonthFromDb(repository = Repositories.sleepRepository)
            _sleepForMonth.emit(getHoursOfSleepForMonthFromDb.execute())

            val getAverageOfSleepForWeek = GetAverageOfSleepForWeek(repository = Repositories.sleepRepository)
            _averageSleepForWeek.emit(getAverageOfSleepForWeek.execute())

            val getAverageOfSleepForMonth = GetAverageOfSleepForMonth(repository = Repositories.sleepRepository)
            _averageSleepForMonth.emit(getAverageOfSleepForMonth.execute())
        }
    }

    fun setGoToSleepTime(time : Long) {
        viewModelScope.launch(AppDispatchers.main) {
            val setGoToSleepTime = SetGoToSleepTime(repository = Repositories.sleepStorage)
            setGoToSleepTime.execute(time = time)
        }
    }

    fun setWakeUpTime(time : Long) {
        viewModelScope.launch(AppDispatchers.main) {
            val setWakeUpTime = SetWakeUpTime(Repositories.sleepStorage)
            setWakeUpTime.execute(time = time)

            val getGoToSleepTime = GetGoToSleepTime(repository = Repositories.sleepStorage)
            val goToSleepTime = getGoToSleepTime.execute()

            if ((goToSleepTime != 0L && time != 0L) && goToSleepTime != time) {
                _sleepForDay.emit(showDiffBetweenTimes(time1 = goToSleepTime, time2 = time))
            }
            else {
                _sleepForDay.emit("")
            }
        }
    }

    private fun calculateTimeBetweenStartEndSleep(time1 : Long, time2 : Long) : Long {
        return if (time1 - time2 <= 0) {
            time2 - time1
        } else {
            val time2Cur = time2 + 86400000
            time2Cur - time1
        }
    }

    private fun showDiffBetweenTimes(time1 : Long, time2 : Long) : String {
        val totalTime = calculateTimeBetweenStartEndSleep(time1 = time1, time2 = time2)
        val hours = abs((totalTime) / 1000 / 60 / 60).toString()
        var minutes = abs((totalTime) / 1000 / 60 % 60).toString()
        if (minutes.length % 2 != 0)
            minutes = "0$minutes"
        return "$hours:$minutes"
    }

}