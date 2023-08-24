package com.example.data.medicines.room

import com.example.data.medicines.entities.MedicinesForDb
import com.example.data.medicines.room.entities.MedicinesDbEntity.Companion.fromAddMedicines
import com.example.data.medicines.room.entities.MedicinesDbEntity.Companion.fromEditMedicines
import com.example.domain.models.Medicines
import com.example.domain.repository.MedicinesRepository
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

    override suspend fun getAllMedicineList(): List<Medicines> {
        return  medicinesDao.getAllMedicineList().map { medicine ->
            medicine.toMedicines()
        }
    }

    override suspend fun createMedicine(medicines: Medicines) {
        val medicine = toMedicinesDbEntity(medicines)
        medicinesDao.createMedicine(fromAddMedicines(medicine))
    }

    override suspend fun updateMedicine(medicines: Medicines) {
        val medicine = toMedicinesDbEntity(medicines)
        medicinesDao.updateMedicine(fromEditMedicines(medicine))
    }

    override suspend fun deleteMedicine(medicines: Medicines) {
        val medicine = toMedicinesDbEntity(medicines)
        medicinesDao.deleteMedicine(fromEditMedicines(medicine))
    }

    private fun toMedicinesDbEntity(medicines : Medicines) : MedicinesForDb {
        return MedicinesForDb(
            id = medicines.id,
            title = medicines.title,
            dateStart = medicines.dateStart,
            durationOfCourse = medicines.durationOfCourse,
            currentDayOfCourse = medicines.currentDayOfCourse,
            timeOfNotify1 = medicines.timeOfNotify1,
            channelIDFirstTime = medicines.channelIDFirstTime,
            timeOfNotify2 = medicines.timeOfNotify2,
            channelIDSecondTime = medicines.channelIDSecondTime,
            timeOfNotify3 = medicines.timeOfNotify3,
            channelIDThirdTime = medicines.channelIDThirdTime,
            timeOfNotify4 = medicines.timeOfNotify4,
            channelIDFourthTime = medicines.channelIDFourthTime,
            totalMissed = medicines.totalMissed,
        )
    }

}