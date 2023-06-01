package com.example.healthcheck.viewmodel

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.healthcheck.databinding.MedicinesItemBinding
import com.example.healthcheck.model.medicines.entities.Medicines

class MedicinesRecyclerViewAdapter : RecyclerView.Adapter<MedicinesRecyclerViewAdapter.MedicinesViewHolder>() {

    var medicinesList : List<Medicines> = emptyList()
        set(newValue) {
            field = newValue
            notifyDataSetChanged()
        }

    class MedicinesViewHolder (
        val binding : MedicinesItemBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MedicinesViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = MedicinesItemBinding.inflate(inflater, parent, false)
        return MedicinesViewHolder(binding)
    }

    override fun getItemCount(): Int = medicinesList.size

    override fun onBindViewHolder(holder: MedicinesViewHolder, position: Int) {
        val medicine = medicinesList[position]
        with(holder.binding) {
            medicinesTitle.text = medicine.title
            date.text = medicine.dateStart
            if (medicine.durationOfCourse != 0) {
                day.text = medicine.currentDayOfCourse.toString() + "/" + medicine.durationOfCourse
            }
            else
                day.text = medicine.currentDayOfCourse.toString() + "/" + "∞"
        }
    }

}