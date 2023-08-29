package com.example.healthcheck.ui.steps

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
import com.example.domain.models.Steps
import com.example.healthcheck.R
import com.example.healthcheck.databinding.DialogEditStepsHistoryBinding
import com.example.healthcheck.databinding.FragmentStepsHistoryBinding
import com.example.healthcheck.util.animations.buttonChangeScreenAnimation.buttonChangeScreenAnimation
import com.example.healthcheck.viewmodels.steps.StepsActionListener
import com.example.healthcheck.viewmodels.steps.StepsHistoryRecyclerViewAdapter
import com.example.healthcheck.viewmodels.steps.StepsHistoryViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch

class StepsHistory : Fragment() {

    companion object {
        fun newInstance() = StepsHistory()
    }

    private lateinit var viewModel : StepsHistoryViewModel
    private lateinit var binding : FragmentStepsHistoryBinding
    private lateinit var adapter : StepsHistoryRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStepsHistoryBinding.inflate(inflater)
        viewModel = ViewModelProvider(this).get(StepsHistoryViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navigation = findNavController()

        val layoutManager = LinearLayoutManager(this.requireContext())
        adapter = StepsHistoryRecyclerViewAdapter(object : StepsActionListener {

            override fun onBoxClickAction(steps: Steps) {
                showAlertDialog(steps)
            }

        })

        lifecycleScope.launch(AppDispatchers.main) {
            binding.stepsHistoryRecyclerView.adapter = adapter
            binding.stepsHistoryRecyclerView.layoutManager = layoutManager
            viewModel.stepsListHistory.collect { list ->
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
                .setPopUpTo(R.id.stepsFragment, true)
                .build()
            //навигация и анимации
            val direction = StepsHistoryDirections.actionStepsHistoryToStepsFragment()
            val navigate = { nav: NavController, d: NavDirections, n: NavOptions -> nav.navigate(d, n) }
            buttonChangeScreenAnimation(binding.wentBack, navigation, direction, navOptions, navigate)
        }

    }

    private fun showAlertDialog(steps: Steps) {
        val inflater = this.layoutInflater
        val dialogBinding = DialogEditStepsHistoryBinding.inflate(inflater)
        val dialogView = dialogBinding.root
        val layout = dialogBinding.textInputLayout
        dialogBinding.oldValueText.text = "${this.context?.getString(R.string.oldValue)} ${steps.countOfSteps}"

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
                if (enteredText.toInt() in 0..265000) {
                    viewModel.updateSteps(steps = steps, enteredText.toInt())
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