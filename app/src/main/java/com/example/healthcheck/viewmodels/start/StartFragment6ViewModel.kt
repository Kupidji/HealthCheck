package com.example.healthcheck.viewmodels.start

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.Repositories
import com.example.domain.AppDispatchers
import com.example.domain.models.Sleep
import com.example.domain.models.Steps
import com.example.domain.usecase.profile.SetHeight
import com.example.domain.usecase.sleep.InsertSleep
import com.example.domain.usecase.start.SetFirstLaunchCompleted
import com.example.domain.usecase.start.SetTimeOfRegularNotification
import com.example.domain.usecase.steps.InsertSteps
import kotlinx.coroutines.launch

class StartFragment6ViewModel : ViewModel() {

    fun setHeight(height : Float) {
        viewModelScope.launch(AppDispatchers.main) {
            val setHeight = SetHeight(repository = Repositories.profileStorage)
            setHeight.execute(height = height)
        }
    }

    fun setFirstLaunchCompleted() {
        viewModelScope.launch(AppDispatchers.main) {
            val setFirstLaunchCompleted = SetFirstLaunchCompleted(repository = Repositories.settingsStorage)
            setFirstLaunchCompleted.execute(completed = false)
        }
    }

    fun setTimeOfRegularNotification(time : Long) {
        viewModelScope.launch(AppDispatchers.main) {
            val setTimeOfRegularNotification = SetTimeOfRegularNotification(repository = Repositories.settingsStorage)
            setTimeOfRegularNotification.execute(time = time)
        }
    }

    fun insertSteps() {
        viewModelScope.launch(AppDispatchers.main) {
            val insertSteps = InsertSteps(repository = Repositories.stepsRepository)
            insertSteps.execute(
                Steps(
                    id = 0,
                    countOfSteps = 0,
                    date = System.currentTimeMillis()
                )
            )
        }
    }

    fun insertHoursOfSleep() {
        viewModelScope.launch(AppDispatchers.main) {
            val insertSleep = InsertSleep(repository = Repositories.sleepRepository)
            insertSleep.execute(
                Sleep(
                    id = 0,
                    timeOfSleep = 0,
                    date = System.currentTimeMillis()
                )
            )
        }
    }

}