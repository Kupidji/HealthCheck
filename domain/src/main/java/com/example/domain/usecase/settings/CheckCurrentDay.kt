package com.example.domain.usecase.settings

import com.example.domain.AppDispatchers
import com.example.domain.repository.SettingsStorageRepository
import com.example.domain.repository.SleepStorageRepository
import com.example.domain.repository.StepsRepository
import com.example.domain.repository.WeightRepository
import com.example.domain.usecase.sleep.SetGoToSleepTime
import com.example.domain.usecase.sleep.SetWakeUpTime
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar

class CheckCurrentDay(
    private val settingsRepository: SettingsStorageRepository,
    private val stepsRepository: StepsRepository,
    private val sleepStorageRepository: SleepStorageRepository,
    private val weightRepository: WeightRepository,
) {

    suspend fun execute() = withContext(AppDispatchers.io) {
        val currentDayLong = Calendar.getInstance().timeInMillis
        var currentDay = SimpleDateFormat("dd.MM").format(currentDayLong)
        val dayInStorage = SimpleDateFormat("dd.MM").format(settingsRepository.getCurrentDate())
        if (currentDay != dayInStorage) {
            settingsRepository.setCurrentDate(currentDayLong)
            //сущность для steps
//            val insertSteps = InsertSteps(repository = stepsRepository)
//            insertSteps.execute(
//                Steps(
//                    id = 0,
//                    countOfSteps = 0,
//                    date = currentDayLong
//                )
//            )

            //сущность для weight, вставляет вчерашнее значение
//            val getWeightForDayFromDb = GetWeightForDayFromDb(repository = weightRepository)
//            val insertWeight = InsertWeight(repository = weightRepository)
//            val weight = getWeightForDayFromDb.execute()
//            insertWeight.execute(
//                Weight(
//                    id = 0,
//                    weight = weight,
//                    date = currentDayLong
//                )
//            )

            //обнуляет время подсчета сна на экране
            val setGoToSleepTime = SetGoToSleepTime(repository = sleepStorageRepository)
            setGoToSleepTime.execute(time = 0L)
            val setWakeUpTime = SetWakeUpTime(repository = sleepStorageRepository)
            setWakeUpTime.execute(time = 0L)

        }

    }

}