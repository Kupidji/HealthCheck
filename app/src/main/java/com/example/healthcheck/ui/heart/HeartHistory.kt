package com.example.healthcheck.ui.heart

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
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
import com.example.domain.models.Heart
import com.example.healthcheck.R
import com.example.healthcheck.databinding.DialogEditHeartHistoryBinding
import com.example.healthcheck.databinding.FragmentHeartHistoryBinding
import com.example.healthcheck.util.animations.buttonChangeScreenAnimation.buttonChangeScreenAnimation
import com.example.healthcheck.viewmodels.heart.HeartActionListener
import com.example.healthcheck.viewmodels.heart.HeartHistoryViewModel
import com.example.healthcheck.viewmodels.heart.HeartRecyclerViewAdapter
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch

class HeartHistory : Fragment() {

    companion object {
        fun newInstance() = HeartHistory()
    }

    private lateinit var viewModel : HeartHistoryViewModel
    private lateinit var binding : FragmentHeartHistoryBinding
    private lateinit var adapter : HeartRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHeartHistoryBinding.inflate(inflater)
        viewModel = ViewModelProvider(this).get(HeartHistoryViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navigation = findNavController()

        val layoutManager = LinearLayoutManager(this.requireContext())
        adapter = HeartRecyclerViewAdapter(object : HeartActionListener {
            override fun onBoxClickAction(heart: Heart) {
                showAlertDialog(heart = heart)
            }

        })

        lifecycleScope.launch(AppDispatchers.main) {
            binding.heartHistoryRecyclerView.layoutManager = layoutManager
            binding.heartHistoryRecyclerView.adapter = adapter
            viewModel.historyHeartList.collect { list ->
                adapter.cardioList = list.reversed()
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
                .setPopUpTo(R.id.heartFragment, true)
                .build()
            //навигация и анимации
            val direction = HeartHistoryDirections.actionHeartHistoryToHeartFragment()
            val navigate = { nav: NavController, d: NavDirections, n: NavOptions -> nav.navigate(d, n) }
            buttonChangeScreenAnimation(binding.wentBack, navigation, direction, navOptions, navigate)
        }

    }

    private fun showAlertDialog(heart: Heart) {
        val inflater = this.layoutInflater
        val dialogBinding = DialogEditHeartHistoryBinding.inflate(inflater)
        val dialogView = dialogBinding.root
        dialogBinding.oldValueText1.text = "${heart.pressureUp}"
        dialogBinding.oldValueText2.text = "${heart.pressureDown}"
        dialogBinding.oldValueText3.text = "${heart.pulse}"

        val dialog = MaterialAlertDialogBuilder(this.requireContext(), com.google.android.material.R.style.MaterialAlertDialog_Material3)
            .setView(dialogView)
            .setTitle(this.context?.getString(R.string.editHistory))
            .setPositiveButton(this.context?.getString(R.string.save), null)
            .setNegativeButton("Отмена", null)
            .show()

        dialogBinding.editTextPressureUp.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                try {
                    if (s.toString().startsWith("1") || s.toString().startsWith("2")) {
                        if (s.toString().length == 3) {
                            dialogBinding.editTextPressureDown.showKeyboard()
                        }
                    }
                    else {
                        if (s.toString().length == 2) {
                            dialogBinding.editTextPressureDown.showKeyboard()
                        }
                    }
                }
                catch (e : StringIndexOutOfBoundsException) {

                }

            }

            override fun afterTextChanged(s: Editable?) {

            }

        })

        dialogBinding.editTextPressureDown.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                try {
                    if (s.toString().startsWith("1") || s.toString().startsWith("2")) {
                        if (s.toString().length == 3) {
                            dialogBinding.editTextPulse.showKeyboard()
                        }
                    }
                    else {
                        if (s.toString().length == 2) {
                            dialogBinding.editTextPulse.showKeyboard()
                        }
                    }
                }
                catch (e : StringIndexOutOfBoundsException) {

                }

            }

            override fun afterTextChanged(s: Editable?) {
                if (s.toString().isEmpty()) {
                    dialogBinding.editTextPressureUp.showKeyboard()
                }
            }

        })

        dialogBinding.editTextPulse.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                try {
                    if (s.toString().startsWith("1") || s.toString().startsWith("2")) {
                        if (s.toString().length == 3) {
                            dialogBinding.editTextPulse.hideKeyboard()
                        }
                    }
                    else {
                        if (s.toString().length == 2) {
                            dialogBinding.editTextPulse.hideKeyboard()
                        }
                    }
                }
                catch (e : StringIndexOutOfBoundsException) {

                }
            }

            override fun afterTextChanged(s: Editable?) {
                if (s.toString().isEmpty()) {
                    dialogBinding.editTextPressureDown.showKeyboard()
                }
            }

        })

        val positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
        positiveButton.setOnClickListener {
            val enteredText1 = dialogBinding.editTextPressureUp.text.toString()
            val enteredText2 = dialogBinding.editTextPressureDown.text.toString()
            val enteredText3 = dialogBinding.editTextPulse.text.toString()
            var status = true

            if (
                checkError(editText = dialogBinding.editTextPressureUp, enteredText1) &&
                checkError(editText = dialogBinding.editTextPressureDown, enteredText2) &&
                checkError(editText = dialogBinding.editTextPulse, enteredText3)
            ) {
                status = false
                viewModel.updateHeart(heart = heart, upPressure = enteredText1.toInt(), downPressure = enteredText2.toInt(), pulse = enteredText3.toInt())
                Toast.makeText(
                    this.requireContext(),
                    this.context?.getString(R.string.historyUpdated),
                    Toast.LENGTH_SHORT
                ).show()
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

    private fun EditText.showKeyboard() {
        if (requestFocus()) {
            (activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
                .showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
            setSelection(text.length)
        }
    }

    private fun EditText.hideKeyboard() {
        if (requestFocus()) {
            (activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
                .hideSoftInputFromWindow(this.windowToken, 0)
            setSelection(text.length)
        }
    }

    private fun checkError(editText : EditText, string : String) : Boolean {
        var value: Int
        if (string.isNotEmpty()) {
            value = string.toInt()
        }
        else {
            editText.error = context?.getString(R.string.emptyString)
            return false
        }

        return when (value) {
            in 30..230 -> {
                true
            }

            else -> {
                editText.error = context?.getString(R.string.wrongValue)
                false
            }
        }

    }

}