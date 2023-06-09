package com.example.healthcheck.model.medicines.room

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.example.healthcheck.model.medicines.MedicinesRepository
import com.example.healthcheck.model.medicines.entities.Medicines
import com.example.healthcheck.model.medicines.room.entities.MedicinesDbEntity.Companion.fromAddMedicines
import com.example.healthcheck.model.medicines.room.entities.MedicinesDbEntity.Companion.fromEditMedicines

class RoomMedicinesRepository (
    val medicinesDao: MedicinesDao
) : MedicinesRepository {

    override fun getAllMedicines(): LiveData<List<Medicines>> {
        return medicinesDao.getAllMedicine().map { list ->
            list.map { medicine ->
                medicine.toMedicines()
            }
        }
    }

    override suspend fun getAllMedicineList(): List<Medicines> {
        return  medicinesDao.getAllMedicineList().map { medicine ->
            medicine.toMedicines()
        }
    }

    override suspend fun createMedicine(medicines: Medicines) {
        medicinesDao.createMedicine(fromAddMedicines(medicines))
    }

    override suspend fun updateMedicine(medicines: Medicines) {
        //подумать, не изменяется ли startDate
        medicinesDao.updateMedicine(fromAddMedicines(medicines))
    }

    override suspend fun deleteMedicine(medicines: Medicines) {
        medicinesDao.deleteMedicine(fromEditMedicines(medicines))
    }


}