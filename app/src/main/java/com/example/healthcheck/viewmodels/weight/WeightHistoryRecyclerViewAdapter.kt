package com.example.healthcheck.viewmodels.weight

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.models.Weight
import com.example.healthcheck.R
import com.example.healthcheck.databinding.WeightItemBinding
import java.text.SimpleDateFormat
import java.util.Locale

class WeightHistoryRecyclerViewAdapter(
    private val weightActionsListener: WeightActionsListener
) : RecyclerView.Adapter<WeightHistoryRecyclerViewAdapter.WeightViewHolder>(), View.OnClickListener {

    class WeightViewHolder(
        val binding : WeightItemBinding
    ) : RecyclerView.ViewHolder(binding.root)

    var list = emptyList<Weight>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeightViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = WeightItemBinding.inflate(inflater, parent, false)

        binding.itemBox.setOnClickListener(this)

        return WeightViewHolder(binding)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: WeightViewHolder, position: Int) {
        val weight = list[position]

        with(holder.binding) {
            itemBox.tag = weight
        }

        with(holder.binding) {
            weightText.text = String.format(Locale.US,"%.1f", weight.weight)
            dateText.text = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(weight.date)
        }
    }

    override fun onClick(view: View) {
        val weight = view.tag as Weight
        when(view.id) {

            R.id.itemBox -> {

                weightActionsListener.onBoxClickAction(weight = weight)

            }

        }
    }

}

interface WeightActionsListener {

    fun onBoxClickAction(weight : Weight)

}