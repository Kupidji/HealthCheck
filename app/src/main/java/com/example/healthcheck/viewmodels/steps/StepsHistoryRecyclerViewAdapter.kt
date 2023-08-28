package com.example.healthcheck.viewmodels.steps

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.models.Steps
import com.example.healthcheck.R
import com.example.healthcheck.databinding.StepsItemBinding
import java.text.SimpleDateFormat
import java.util.Locale

class StepsHistoryRecyclerViewAdapter(
    private val stepsActionListener: StepsActionListener
) : RecyclerView.Adapter<StepsHistoryRecyclerViewAdapter.StepsViewHolder>(), View.OnClickListener {

    class StepsViewHolder(
        val binding : StepsItemBinding
    ) : RecyclerView.ViewHolder(binding.root)

    var list = emptyList<Steps>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StepsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = StepsItemBinding.inflate(inflater, parent, false)

        binding.itemBox.setOnClickListener(this)

        return StepsViewHolder(binding)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: StepsViewHolder, position: Int) {
        val steps = list[position]
        with(holder.binding) {
            itemBox.tag = steps
        }
        //заполнение item
        with(holder.binding) {
            stepsText.text = steps.countOfSteps.toString()
            dateText.text = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(steps.date)
        }
    }

    override fun onClick(view: View) {
        val steps = view.tag as Steps

        when(view.id) {

            R.id.itemBox -> {
                stepsActionListener.onBoxClickAction(steps = steps)
            }

        }

    }

}

interface StepsActionListener {

    fun onBoxClickAction(steps : Steps)

}