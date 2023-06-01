package com.example.healthcheck.model.medicines.room

import com.example.healthcheck.model.medicines.MedicinesRepository
import com.example.healthcheck.model.medicines.entities.Medicines
import com.example.healthcheck.model.medicines.room.entities.MedicinesDbEntity.Companion.fromAddMedicines
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RoomMedicinesRepository (
    val medicinesDao: MedicinesDao
) : MedicinesRepository {

    override fun getAllMedicines(): Flow<List<Medicines>> {
        return medicinesDao.getAllMedicine().map { list ->
            list.map { medicine ->
                medicine.toMedicines()
            }
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
        medicinesDao.deleteMedicine(fromAddMedicines(medicines))
    }
}