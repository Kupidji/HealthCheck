package com.example.healthcheck.model.medicines.viewmodel

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.healthcheck.R
import com.example.healthcheck.databinding.SearchMedicineItemBinding

class MedicinesSearchRecyclerViewAdapter(
    private val addMedicinesActionListener: AddMedicinesActionListener
) : RecyclerView.Adapter<MedicinesSearchRecyclerViewAdapter.MedicinesSearchViewHolder>(), View.OnClickListener {

    var medicinesSearchList : List<List<String>> = emptyList()
        set(newValue) {
            field = newValue
            notifyDataSetChanged()
        }

    class MedicinesSearchViewHolder(
        val binding : SearchMedicineItemBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MedicinesSearchViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = SearchMedicineItemBinding.inflate(inflater, parent, false)

        binding.root.setOnClickListener(this)
        binding.itemBox.setOnClickListener(this)

        return MedicinesSearchViewHolder(binding)
    }

    override fun getItemCount(): Int = medicinesSearchList.size

    override fun onBindViewHolder(holder: MedicinesSearchViewHolder, position: Int) {
        with(holder.binding) {
            itemBox.tag = medicinesSearchList.get(position).get(0)
            root.tag = medicinesSearchList.get(position).get(0)
            titleText.text = medicinesSearchList.get(position).get(0)
        }
    }

    override fun onClick(v: View) {
        var title = v.tag as String
        when (v.id) {

            R.id.itemBox -> {
                addMedicinesActionListener.onClickBox(title)
            }

        }
    }

}

interface AddMedicinesActionListener {

    fun onClickBox(title : String)

}