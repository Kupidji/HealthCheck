package com.example.healthcheck.viewmodel

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.healthcheck.R
import com.example.healthcheck.databinding.MedicinesItemBinding
import com.example.healthcheck.model.medicines.entities.Medicines

class MedicinesRecyclerViewAdapter(
    private val medicinesActionListener: MedicinesActionListener
) : RecyclerView.Adapter<MedicinesRecyclerViewAdapter.MedicinesViewHolder>(), View.OnClickListener {

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

        binding.root.setOnClickListener(this)
        binding.checkbox.setOnClickListener(this)
        binding.itemBackgroundBtn.setOnClickListener(this)

        return MedicinesViewHolder(binding)
    }

    override fun getItemCount(): Int = medicinesList.size

    override fun onBindViewHolder(holder: MedicinesViewHolder, position: Int) {
        val medicine = medicinesList[position]
        with(holder.binding) {
            holder.itemView.tag = medicine
            checkbox.tag = medicine
            root.tag = medicine
            itemBackgroundBtn.tag = medicine

            medicinesTitle.text = medicine.title
            date.text = medicine.dateStart
            if (medicine.durationOfCourse != 0) {
                day.text = medicine.currentDayOfCourse.toString() + "/" + medicine.durationOfCourse
            }
            else
                day.text = medicine.currentDayOfCourse.toString() + "/" + "∞"
        }
    }

    override fun onClick(view : View) {
        val medicine = view.tag as Medicines
        when(view.id) {

            R.id.checkbox -> {
                medicinesActionListener.onClickCheckBox(medicine)
            }

            R.id.itemBackgroundBtn -> {
                medicinesActionListener.onClickBox(medicine)
            }

        }
    }

}

interface MedicinesActionListener {

    fun onClickBox(medicines: Medicines)

    fun onClickCheckBox(medicines: Medicines)

}