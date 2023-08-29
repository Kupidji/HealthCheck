package com.example.healthcheck.ui.weight

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.domain.AppDispatchers
import com.example.domain.models.Weight
import com.example.healthcheck.R
import com.example.healthcheck.databinding.DialogEditWeightHistoryBinding
import com.example.healthcheck.databinding.FragmentWeightHistoryBinding
import com.example.healthcheck.util.animations.buttonChangeScreenAnimation.buttonChangeScreenAnimation
import com.example.healthcheck.viewmodels.weight.WeightActionsListener
import com.example.healthcheck.viewmodels.weight.WeightHistoryRecyclerViewAdapter
import com.example.healthcheck.viewmodels.weight.WeightHistoryViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch

class WeightHistory : Fragment() {

    companion object {
        fun newInstance() = WeightHistory()
    }

    private lateinit var viewModel : WeightHistoryViewModel
    private lateinit var binding : FragmentWeightHistoryBinding
    private lateinit var adapter : WeightHistoryRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWeightHistoryBinding.inflate(inflater)
        viewModel = ViewModelProvider(this).get(WeightHistoryViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navigation = findNavController()

        val layoutManager = LinearLayoutManager(this.requireContext())
        adapter = WeightHistoryRecyclerViewAdapter(object : WeightActionsListener {
            override fun onBoxClickAction(weight: Weight) {
                showAlertDialog(weight = weight)
            }

        })

        lifecycleScope.launch(AppDispatchers.main) {
            binding.weightHistoryRecyclerView.adapter = adapter
            binding.weightHistoryRecyclerView.layoutManager = layoutManager
            viewModel.weightListHistory.collect { list ->
                adapter.list = list
                if (list.isEmpty()) {
                    binding.nothingThere.visibility = View.VISIBLE
                }
                else {
                    binding.nothingThere.visibility = View.GONE
                }
            }
        }

        binding.wentBack.setOnClickListener {
            var navOptions = NavOptions.Builder()
                .setEnterAnim(androidx.navigation.ui.R.anim.nav_default_enter_anim)
                .setExitAnim(androidx.navigation.ui.R.anim.nav_default_exit_anim)
                .setPopEnterAnim(androidx.navigation.ui.R.anim.nav_default_pop_enter_anim)
                .setPopExitAnim(androidx.navigation.ui.R.anim.nav_default_pop_exit_anim)
                .setPopUpTo(R.id.weightFragment, true)
                .build()
            //навигация и анимации
            val direction = WeightHistoryDirections.actionWeightHistoryToWeightFragment()
            val navigate = { nav: NavController, d: NavDirections, n: NavOptions -> nav.navigate(d, n) }
            buttonChangeScreenAnimation(binding.wentBack, navigation, direction, navOptions, navigate)
        }

    }

    private fun showAlertDialog(weight: Weight) {
        val inflater = this.layoutInflater
        val dialogBinding = DialogEditWeightHistoryBinding.inflate(inflater)
        val dialogView = dialogBinding.root
        val layout = dialogBinding.textInputLayout
        dialogBinding.oldValueText.text = "${this.context?.getString(R.string.oldValue)} ${weight.weight}"

        val dialog = MaterialAlertDialogBuilder(this.requireContext(), com.google.android.material.R.style.MaterialAlertDialog_Material3)
            .setView(dialogView)
            .setTitle(this.context?.getString(R.string.editHistory))
            .setPositiveButton(this.context?.getString(R.string.save), null)
            .setNegativeButton("Отмена", null)
            .show()

        val positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
        positiveButton.setOnClickListener {
            val enteredText = dialogBinding.editText.text.toString()
            var status = true

            if (enteredText.isNotEmpty()) {
                if (enteredText.toFloat() in 1.0..635.0) {
                    viewModel.updateWeight(weight = weight, enteredText.toFloat())
                    Toast.makeText(
                        this.requireContext(),
                        this.context?.getString(R.string.historyUpdated),
                        Toast.LENGTH_SHORT
                    ).show()
                    status = false
                } else {
                    layout.error = this.context?.getString(R.string.wrongValue)
                }
            } else {
                layout.error = this.context?.getString(R.string.emptyString)
            }

            if (!status) {
                dialog.dismiss()
            }
        }

        val negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
        negativeButton.setOnClickListener {
            dialog.dismiss()
        }

    }

}