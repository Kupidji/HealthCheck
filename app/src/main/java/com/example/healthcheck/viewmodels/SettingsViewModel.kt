package com.example.healthcheck.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.Repositories
import com.example.domain.AppDispatchers
import com.example.domain.usecase.settings.GetNutritionVisibility
import com.example.domain.usecase.settings.SetApplicationTheme
import com.example.domain.usecase.settings.SetNutritionVisibility
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class SettingsViewModel : ViewModel() {

    private val _nutritionVisibility = MutableSharedFlow<Boolean>(1, 0, BufferOverflow.DROP_OLDEST)
    val nutritionVisibility = _nutritionVisibility.asSharedFlow()

    init {
        viewModelScope.launch(AppDispatchers.main) {
            val getNutritionVisibility = GetNutritionVisibility(repository = Repositories.settingsStorage)
            _nutritionVisibility.emit(getNutritionVisibility.execute())
        }

    }
    fun setTheme(theme : Int) {
        viewModelScope.launch(AppDispatchers.main) {
            val setApplicationTheme = SetApplicationTheme(repository = Repositories.settingsStorage)
            setApplicationTheme.execute(theme = theme)
        }
    }

    fun setNutritionVisibility(visibility : Boolean) {
        viewModelScope.launch(AppDispatchers.main) {
            val setNutritionVisibility = SetNutritionVisibility(repository = Repositories.settingsStorage)
            setNutritionVisibility.execute(visibility = visibility)
        }
    }

}