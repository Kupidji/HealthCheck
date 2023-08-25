package com.example.healthcheck.viewmodels.start

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.Repositories
import com.example.domain.AppDispatchers
import com.example.domain.models.Weight
import com.example.domain.usecase.profile.SetAge
import com.example.domain.usecase.weight.InsertWeight
import kotlinx.coroutines.launch
import java.util.Calendar

class StartFragment5ViewModel : ViewModel() {

    fun insertWeight(weight: Float) {
        viewModelScope.launch(AppDispatchers.main) {
            val insertWeight = InsertWeight(repository = Repositories.weightRepository)

            val currentDate = Calendar.getInstance().timeInMillis
            val ourWeight = Weight(
                id = 0,
                weight = weight,
                date = currentDate,
            )
            insertWeight.execute(weight = ourWeight)
        }
    }

    fun setAge(age : Int) {
        viewModelScope.launch(AppDispatchers.main) {
            val setAge = SetAge(repository = Repositories.profileStorage)
            setAge.execute(age = age)
        }
    }

}