package com.example.healthcheck.viewmodels.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.Repositories
import com.example.domain.AppDispatchers
import com.example.domain.models.Medicines
import com.example.domain.usecase.GetNearestAction
import com.example.domain.usecase.settings.GetNutritionVisibility
import com.example.domain.usecase.start.GetFirstLaunchCompleted
import com.example.healthcheck.ui.main.mainFragment1
import com.example.healthcheck.ui.main.mainFragment2
import com.example.healthcheck.ui.main.mainFragment3
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val _nearestAction = MutableSharedFlow<Medicines>(1, 0, BufferOverflow.DROP_OLDEST)
    val nearestAction = _nearestAction.asSharedFlow()

    private val _nearestTime = MutableSharedFlow<String>(1, 0, BufferOverflow.DROP_OLDEST)
    val nearestTime = _nearestTime.asSharedFlow()

    private val _visibilityOfNutrition = MutableSharedFlow<Boolean>(1, 0, BufferOverflow.DROP_OLDEST)
    val visibilityOfNutrition = _visibilityOfNutrition.asSharedFlow()

    private val _isFirstLaunch = MutableSharedFlow<Boolean>(1, 0, BufferOverflow.DROP_OLDEST)
    val isFirstLaunch = _isFirstLaunch.asSharedFlow()

    val fragList = listOf(
        mainFragment1.newInstance(),
        mainFragment2.newInstance(),
        mainFragment3.newInstance(),
    )

    init {
        viewModelScope.launch(AppDispatchers.main) {
            val getNearestAction = GetNearestAction(repository = Repositories.medicinesRepository)
            getNearestAction.execute()

            _nearestAction.emit(getNearestAction._nearestAction)
            _nearestTime.emit(getNearestAction._nearestTime)
        }

        viewModelScope.launch(AppDispatchers.main) {
            val getNutritionVisibility = GetNutritionVisibility(repository = Repositories.settingsStorage)
            _visibilityOfNutrition.emit(getNutritionVisibility.execute())
        }

        viewModelScope.launch(AppDispatchers.main) {
            val getFirstLaunchCompleted = GetFirstLaunchCompleted(repository = Repositories.settingsStorage)
            _isFirstLaunch.emit(getFirstLaunchCompleted.execute())
        }

    }

}