package com.example.healthcheck.viewmodels.weight

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.Repositories
import com.example.domain.AppDispatchers
import com.example.domain.models.Weight
import com.example.domain.usecase.GetWeightForDayFromDb
import com.example.domain.usecase.GetWeightForMonthFromDb
import com.example.domain.usecase.GetWeightForWeekFromDb
import com.example.domain.usecase.weight.CalculateBodyMassIndex
import com.example.domain.usecase.weight.CalculateContentOfFatCommonMethod
import com.example.domain.usecase.weight.GetForearm
import com.example.domain.usecase.weight.GetHips
import com.example.domain.usecase.weight.GetLeftHip
import com.example.domain.usecase.weight.GetNeck
import com.example.domain.usecase.weight.GetRightHip
import com.example.domain.usecase.weight.GetShin
import com.example.domain.usecase.weight.GetWaist
import com.example.domain.usecase.weight.GetWrist
import com.example.domain.usecase.weight.InsertWeight
import com.example.domain.usecase.weight.SetForearm
import com.example.domain.usecase.weight.SetHips
import com.example.domain.usecase.weight.SetLeftHip
import com.example.domain.usecase.weight.SetNeck
import com.example.domain.usecase.weight.SetRightHip
import com.example.domain.usecase.weight.SetShin
import com.example.domain.usecase.weight.SetWaist
import com.example.domain.usecase.weight.SetWrist
import com.example.domain.usecase.weight.UpdateWeight
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar

class WeightViewModel : ViewModel() {

    private val _totalWeightForDay = MutableSharedFlow<Float>(1, 0, BufferOverflow.DROP_OLDEST)
    val totalWeightForDay = _totalWeightForDay.asSharedFlow()

    private val _totalWeightForWeek = MutableSharedFlow<Float>(1, 0, BufferOverflow.DROP_OLDEST)
    val totalWeightForWeek = _totalWeightForWeek.asSharedFlow()

    private val _totalWeightForMonth = MutableSharedFlow<Float>(1, 0, BufferOverflow.DROP_OLDEST)
    val totalWeightForMonth = _totalWeightForMonth.asSharedFlow()

    private val _id = MutableSharedFlow<Int>(1, 0, BufferOverflow.DROP_OLDEST)
    val id = _id.asSharedFlow()

    private val _date = MutableSharedFlow<Long>(1, 0, BufferOverflow.DROP_OLDEST)
    val date = _date.asSharedFlow()

    private val _neck = MutableSharedFlow<Float>(1, 0, BufferOverflow.DROP_OLDEST)
    val neck = _neck.asSharedFlow()

    private val _waist = MutableSharedFlow<Float>(1, 0, BufferOverflow.DROP_OLDEST)
    val waist = _waist.asSharedFlow()

    private val _forearm = MutableSharedFlow<Float>(1, 0, BufferOverflow.DROP_OLDEST)
    val forearm = _forearm.asSharedFlow()

    private val _wrist = MutableSharedFlow<Float>(1, 0, BufferOverflow.DROP_OLDEST)
    val wrist = _wrist.asSharedFlow()

    private val _bothHips = MutableSharedFlow<Float>(1, 0, BufferOverflow.DROP_OLDEST)
    val bothHips = _bothHips.asSharedFlow()

    private val _leftHip = MutableSharedFlow<Float>(1, 0, BufferOverflow.DROP_OLDEST)
    val leftHip = _leftHip.asSharedFlow()

    private val _rightHip = MutableSharedFlow<Float>(1, 0, BufferOverflow.DROP_OLDEST)
    val rightHip = _rightHip.asSharedFlow()

    private val _shin = MutableSharedFlow<Float>(1, 0, BufferOverflow.DROP_OLDEST)
    val shin = _shin.asSharedFlow()

    private val _bodyMassIndex = MutableSharedFlow<Float>(1, 0, BufferOverflow.DROP_OLDEST)
    val bodyMassIndex = _bodyMassIndex.asSharedFlow()

    private val _contentOfFat = MutableSharedFlow<Float>(1, 0, BufferOverflow.DROP_OLDEST)
    val contentOfFat = _contentOfFat.asSharedFlow()

    private var currentDate = Calendar.getInstance().timeInMillis

    init {
        viewModelScope.launch(AppDispatchers.main) {
            val getWeightForDayFromDb = GetWeightForDayFromDb(repository = Repositories.weightRepository)
            val currentDate = SimpleDateFormat("dd.MM").format(Calendar.getInstance().timeInMillis)
            val lastDate = SimpleDateFormat("dd.MM").format(getLastDateFromDataWeight())
            if (lastDate == currentDate) {
                _totalWeightForDay.emit(getWeightForDayFromDb.execute())
            }
            else {
                _totalWeightForDay.emit(0F)
            }

        }

        viewModelScope.launch(AppDispatchers.main) {
            val getWeightForWeekFromDb = GetWeightForWeekFromDb(repository = Repositories.weightRepository)
            _totalWeightForWeek.emit(getWeightForWeekFromDb.execute())
        }

        viewModelScope.launch(AppDispatchers.main) {
            val getWeightForMonthFromDb = GetWeightForMonthFromDb(repository = Repositories.weightRepository)
            _totalWeightForMonth.emit(getWeightForMonthFromDb.execute())
        }

        viewModelScope.launch(AppDispatchers.main) {
            _id.emit(getLastIdFromDataWeight())
        }

        viewModelScope.launch(AppDispatchers.main) {
            _date.emit(getLastDateFromDataWeight())
        }

        viewModelScope.launch(AppDispatchers.main) {
            val getNeck = GetNeck(repository = Repositories.weightStorage)
            _neck.emit(getNeck.execute())
        }

        viewModelScope.launch(AppDispatchers.main) {
            val getWaist = GetWaist(repository = Repositories.weightStorage)
            _waist.emit(getWaist.execute())
        }

        viewModelScope.launch(AppDispatchers.main) {
            val getForearm = GetForearm(repository = Repositories.weightStorage)
            _forearm.emit(getForearm.execute())
        }

        viewModelScope.launch(AppDispatchers.main) {
            val getWrist = GetWrist(repository = Repositories.weightStorage)
            _wrist.emit(getWrist.execute())
        }

        viewModelScope.launch(AppDispatchers.main) {
            val getHips = GetHips(repository = Repositories.weightStorage)
            _bothHips.emit(getHips.execute())
        }

        viewModelScope.launch(AppDispatchers.main) {
            val getLeftHip = GetLeftHip(repository = Repositories.weightStorage)
            _leftHip.emit(getLeftHip.execute())
        }

        viewModelScope.launch(AppDispatchers.main) {
            val getRightHip = GetRightHip(repository = Repositories.weightStorage)
            _rightHip.emit(getRightHip.execute())
        }

        viewModelScope.launch(AppDispatchers.main) {
            val getShin = GetShin(repository = Repositories.weightStorage)
            _shin.emit(getShin.execute())
        }

        viewModelScope.launch(AppDispatchers.main) {
            val calculateBodyMassIndex = CalculateBodyMassIndex(repositoryProfile = Repositories.profileStorage ,repositoryWeight = Repositories.weightRepository)
            _bodyMassIndex.emit(calculateBodyMassIndex.execute())
        }

        viewModelScope.launch(AppDispatchers.main) {
            val calculateContentOfFatCommonMethod = CalculateContentOfFatCommonMethod(repositoryProfile = Repositories.profileStorage ,repositoryWeight = Repositories.weightRepository)
            _contentOfFat.emit(calculateContentOfFatCommonMethod.execute())
        }

    }

    suspend fun getLastDateFromDataWeight() : Long = withContext(AppDispatchers.main) {
        var date = currentDate
        withContext(AppDispatchers.io) {
            if (Repositories.weightRepository.getWeightForDay() != null) {
                date = Repositories.weightRepository.getWeightForDay().date
            }
        }
        return@withContext date
    }

    suspend fun getLastIdFromDataWeight() : Int = withContext(AppDispatchers.main) {
        var id = 0
        withContext(AppDispatchers.main) {
            if (Repositories.weightRepository.getWeightForDay().id != null) {
                id = Repositories.weightRepository.getWeightForDay().id
            }
        }
        return@withContext id
    }

    fun insertWeight(weight: Float) {
        viewModelScope.launch(AppDispatchers.main) {
            val insertWeight = InsertWeight(repository = Repositories.weightRepository)
            insertWeight.execute(weight = Weight(
                    id = 0,
                    weight = weight,
                    date = Calendar.getInstance().timeInMillis
                )
            )
            _id.emit(getLastIdFromDataWeight())
            _date.emit(getLastDateFromDataWeight())
            _totalWeightForDay.emit(weight)

            val getWeightForWeekFromDb = GetWeightForWeekFromDb(repository = Repositories.weightRepository)
            _totalWeightForWeek.emit(getWeightForWeekFromDb.execute())

            val getWeightForMonthFromDb = GetWeightForMonthFromDb(repository = Repositories.weightRepository)
            _totalWeightForMonth.emit(getWeightForMonthFromDb.execute())

            val calculateBodyMassIndex = CalculateBodyMassIndex(repositoryProfile = Repositories.profileStorage ,repositoryWeight = Repositories.weightRepository)
            _bodyMassIndex.emit(calculateBodyMassIndex.execute())

            val calculateContentOfFatCommonMethod = CalculateContentOfFatCommonMethod(repositoryProfile = Repositories.profileStorage ,repositoryWeight = Repositories.weightRepository)
            _contentOfFat.emit(calculateContentOfFatCommonMethod.execute())

        }
    }

    fun updateWeight(id : Int, weight: Float) {
        viewModelScope.launch(AppDispatchers.main) {
            val updateWeight = UpdateWeight(repository = Repositories.weightRepository)
            updateWeight.execute(weight = Weight(
                    id = id,
                    weight = weight,
                    date = Calendar.getInstance().timeInMillis
                )
            )
            _totalWeightForDay.emit(weight)

            val getWeightForWeekFromDb = GetWeightForWeekFromDb(repository = Repositories.weightRepository)
            _totalWeightForWeek.emit(getWeightForWeekFromDb.execute())

            val getWeightForMonthFromDb = GetWeightForMonthFromDb(repository = Repositories.weightRepository)
            _totalWeightForMonth.emit(getWeightForMonthFromDb.execute())

            val calculateBodyMassIndex = CalculateBodyMassIndex(repositoryProfile = Repositories.profileStorage ,repositoryWeight = Repositories.weightRepository)
            _bodyMassIndex.emit(calculateBodyMassIndex.execute())

            val calculateContentOfFatCommonMethod = CalculateContentOfFatCommonMethod(repositoryProfile = Repositories.profileStorage ,repositoryWeight = Repositories.weightRepository)
            _contentOfFat.emit(calculateContentOfFatCommonMethod.execute())

        }
    }

    fun setNeck(neck : Float) {
        viewModelScope.launch(AppDispatchers.main) {
            val setNeck = SetNeck(repository = Repositories.weightStorage)
            setNeck.execute(neck = neck)
            _neck.emit(neck)
        }
    }

    fun setWaist(waist : Float) {
        viewModelScope.launch(AppDispatchers.main) {
            val setWaist = SetWaist(repository = Repositories.weightStorage)
            setWaist.execute(waist = waist)
            _waist.emit(waist)
        }
    }

    fun setForearm(forearm : Float) {
        viewModelScope.launch(AppDispatchers.main) {
            val setForearm = SetForearm(repository = Repositories.weightStorage)
            setForearm.execute(forearm = forearm)
            _forearm.emit(forearm)
        }
    }

    fun setWrist(wrist : Float) {
        viewModelScope.launch(AppDispatchers.main) {
            val setWrist = SetWrist(repository = Repositories.weightStorage)
            setWrist.execute(wrist = wrist)
            _wrist.emit(wrist)
        }
    }

    fun setBothHips(hips : Float) {
        viewModelScope.launch(AppDispatchers.main) {
            val setHips = SetHips(repository = Repositories.weightStorage)
            setHips.execute(hips =  hips)
            _bothHips.emit(hips)
        }
    }

    fun setLeftHip(hip : Float) {
        viewModelScope.launch(AppDispatchers.main) {
            val setHip = SetLeftHip(repository = Repositories.weightStorage)
            setHip.execute(hip = hip)
            _leftHip.emit(hip)
        }
    }

    fun setRightHip(hip : Float) {
        viewModelScope.launch(AppDispatchers.main) {
            val setHip = SetRightHip(repository = Repositories.weightStorage)
            setHip.execute(hip = hip)
            _rightHip.emit(hip)
        }
    }

    fun setShin(shin : Float) {
        viewModelScope.launch(AppDispatchers.main) {
            val setShin = SetShin(repository = Repositories.weightStorage)
            setShin.execute(shin = shin)
            _shin.emit(shin)
        }
    }

}