package com.example.healthcheck.viewmodels

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.models.Medicines
import com.example.healthcheck.R
import com.example.healthcheck.databinding.MedicinesItemBinding
import java.text.SimpleDateFormat

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
        //binding.checkbox.setOnClickListener(this)
        binding.itemBackgroundBtn.setOnClickListener(this)

        return MedicinesViewHolder(binding)
    }

    override fun getItemCount(): Int = medicinesList.size

    override fun onBindViewHolder(holder: MedicinesViewHolder, position: Int) {

        val medicine = medicinesList[position]
        with(holder.binding) {
            holder.itemView.tag = medicine
            //checkbox.tag = medicine
            root.tag = medicine
            itemBackgroundBtn.tag = medicine

            //все, что ниже в теле этого класса - заполняет шаблон item_medicine
            medicinesTitle.text = medicine.title
            date.text = SimpleDateFormat("dd MMM").format(medicine.dateStart)

            // на будущее
            //выставляет ближайшее время пропития таблетки, если оно еще не наступило
            //иначе выставляет следующее (если оно есть)
//            if (getNearestTime(medicine) != 0L) {
//                holder.binding.nearestTime.visibility = View.VISIBLE
//                holder.binding.nearestTime.text = SimpleDateFormat("HH:mm").format(getNearestTime(medicine))
//            }
//            else
//                holder.binding.nearestTime.visibility = View.GONE

            //если у курса задан конец, показываем: текущий день/всего дней
            //иначе: текущий день/беск.
            if (medicine.durationOfCourse != 0) {
                day.text = medicine.currentDayOfCourse.toString() + "/" + medicine.durationOfCourse
                if (medicine.currentDayOfCourse > medicine.durationOfCourse) {
                    day.setTextColor(ContextCompat.getColor(day.context, R.color.textcolor_done))
                    dayText.setTextColor(ContextCompat.getColor(day.context, R.color.textcolor_done))
                    Toast.makeText(day.context, "Курс ${medicine.title} закончен", Toast.LENGTH_SHORT).show()
                }
            }
            else
                day.text = medicine.currentDayOfCourse.toString() + "/" + "∞"
        }

    }

//    private fun getNearestTime(medicines: Medicines) : Long {
//        var currentTime = Calendar.getInstance().timeInMillis
//        var maxTime = 0L
//        var listOfTime = listOf<Long>(
//            medicines.timeOfNotify1,
//            medicines.timeOfNotify2,
//            medicines.timeOfNotify3,
//            medicines.timeOfNotify4,
//        )
//
//        for (time in listOfTime) {
//            if (maxTime < time && time < currentTime)
//                maxTime = time
//        }
//
//        return maxTime
//    }
    override fun onClick(view : View) {
        val medicine = view.tag as Medicines
        when(view.id) {

//            R.id.checkbox -> {
//                medicinesActionListener.onClickCheckBox(medicine)
//            }

            R.id.itemBackgroundBtn -> {
                medicinesActionListener.onClickBox(medicine)
            }

        }
    }

}

interface MedicinesActionListener {

    fun onClickBox(medicines: Medicines)

//    fun onClickCheckBox(medicines: Medicines)

}