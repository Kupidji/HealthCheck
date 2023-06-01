package com.example.healthcheck.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.healthcheck.Repositories
import com.example.healthcheck.model.medicines.entities.Medicines
import kotlinx.coroutines.flow.Flow

class MedicinesViewModel : ViewModel() {

    fun getAllMedicines() : LiveData<List<Medicines>> {
        return Repositories.medicinesRepository.getAllMedicines()
    }

}