package com.example.healthcheck.model.medicines.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.healthcheck.model.Repositories
import com.example.healthcheck.model.medicines.entities.Medicines

class MedicinesViewModel : ViewModel() {

    fun getAllMedicines() : LiveData<List<Medicines>> {
        return Repositories.medicinesRepository.getAllMedicines()
    }

}