package com.example.healthcheck.viewmodel

import androidx.lifecycle.ViewModel
import com.example.healthcheck.model.Repositories
import com.example.healthcheck.model.medicines.entities.Medicines

class AddMedicinesViewModel : ViewModel() {
    suspend fun createMedicine(medicines: Medicines) {
        Repositories.medicinesRepository.createMedicine(medicines)
    }

}