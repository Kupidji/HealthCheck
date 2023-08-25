package com.example.healthcheck.ui

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.domain.AppDispatchers
import com.example.healthcheck.R
import com.example.healthcheck.databinding.FragmentHeartBinding
import com.example.healthcheck.util.animations.buttonChangeScreenAnimation.buttonChangeScreenAnimation
import com.example.healthcheck.viewmodels.heart.HeartRecyclerViewAdapter
import com.example.healthcheck.viewmodels.heart.HeartViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class heartFragment : Fragment() {

    companion object {
        fun newInstance() = heartFragment()
    }

    private lateinit var viewModel: HeartViewModel
    private lateinit var binding : FragmentHeartBinding
    private lateinit var adapter : HeartRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(HeartViewModel::class.java)
        binding = FragmentHeartBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navigation = findNavController()

        val layoutManager = LinearLayoutManager(this.requireContext())

        adapter = HeartRecyclerViewAdapter()

        lifecycleScope.launch(AppDispatchers.main) {
            binding.heartRecyclerView.layoutManager = layoutManager
            binding.heartRecyclerView.adapter = adapter
            viewModel.listOfHeart.collect { list ->
                adapter.cardioList = list
                if (list.isEmpty()) {
                    binding.nothingThere.visibility = View.VISIBLE
                }
                else {
                    binding.nothingThere.visibility = View.GONE
                }
            }
        }


        binding.saveHeartItemBtn.setOnClickListener {
            var navOptions = NavOptions.Builder()
                .setEnterAnim(androidx.navigation.ui.R.anim.nav_default_enter_anim)
                .setExitAnim(androidx.navigation.ui.R.anim.nav_default_exit_anim)
                .setPopEnterAnim(androidx.navigation.ui.R.anim.nav_default_pop_enter_anim)
                .setPopExitAnim(androidx.navigation.ui.R.anim.nav_default_pop_exit_anim)
                .setPopUpTo(R.id.addHeartItemFragment , true)
                .build()
            val direction = heartFragmentDirections.actionHeartFragmentToAddHeartItemFragment()
            val navigate = { nav: NavController, d: NavDirections, n: NavOptions -> nav.navigate(d, n) }
            buttonChangeScreenAnimation(binding.saveHeartItemBtn, navigation, direction, navOptions, navigate)
        }

        binding.wentBack.setOnClickListener {
            var navOptions = NavOptions.Builder()
                .setEnterAnim(androidx.navigation.ui.R.anim.nav_default_enter_anim)
                .setExitAnim(androidx.navigation.ui.R.anim.nav_default_exit_anim)
                .setPopEnterAnim(androidx.navigation.ui.R.anim.nav_default_pop_enter_anim)
                .setPopExitAnim(androidx.navigation.ui.R.anim.nav_default_pop_exit_anim)
                .setPopUpTo(R.id.mainFragment, true)
                .build()
            //навигация и анимации
            val direction = heartFragmentDirections.actionHeartFragmentToMainFragment()
            val navigate = { nav: NavController, d: NavDirections, n: NavOptions -> nav.navigate(d, n) }
            buttonChangeScreenAnimation(binding.wentBack, navigation, direction, navOptions, navigate)
        }


//        var id = 0
//        var date = 0L
//
//        lifecycleScope.launch {
//            viewModel.lastDate.collect {
//                if (it != null) {
//                    date = it
//                }
//            }
//        }
//
//        lifecycleScope.launch {
//            viewModel.lastId.collect {
//                if (it != null) {
//                    id = it
//                }
//            }
//        }
//
//        lifecycleScope.launch {
//            viewModel.upperPressure.collect {
//                binding.ur1.text = it.toString()
//            }
//        }
//
//        lifecycleScope.launch {
//            viewModel.lowerPressure.collect {
//                binding.ur2.text = it.toString()
//            }
//        }
//
//        lifecycleScope.launch {
//            viewModel.pulse.collect {
//                binding.ur3.text = it.toString()
//            }
//        }
//
//        binding.wentBack.setOnClickListener {
//            var navOptions = NavOptions.Builder()
//                .setEnterAnim(androidx.navigation.ui.R.anim.nav_default_enter_anim)
//                .setExitAnim(androidx.navigation.ui.R.anim.nav_default_exit_anim)
//                .setPopEnterAnim(androidx.navigation.ui.R.anim.nav_default_pop_enter_anim)
//                .setPopExitAnim(androidx.navigation.ui.R.anim.nav_default_pop_exit_anim)
//                .setPopUpTo(R.id.mainFragment, true)
//                .build()
//            //навигация и анимации
//            val direction = heartFragmentDirections.actionHeartFragmentToMainFragment()
//            val navigate = { nav: NavController, d: NavDirections, n: NavOptions -> nav.navigate(d, n) }
//            buttonChangeScreenAnimation(binding.wentBack, navigation, direction, navOptions, navigate)
//        }
//
//        view.viewTreeObserver.addOnGlobalLayoutListener {
//            val r = Rect()
//            view.getWindowVisibleDisplayFrame(r)
//            val heightDiff = view.rootView.height - r.height()
//
//            //Если клавиатура убрана
//            if (heightDiff < 0.2 * view.rootView.height) {
//
//                //Скрыть фокус
//                forFocus(false)
//
//                //сохранение в бд, когда все 3 заполнены
//                afterKeyboardIsDown(binding.getUpPressure, Constants.UPPER, id, date)
//                afterKeyboardIsDown(binding.getDownPressure, Constants.LOWER, id, date)
//                afterKeyboardIsDown(binding.getPulse, Constants.PULSE, id, date)
//
//
//            } else {
//                //Показать фокус
//                forFocus(true)
//            }
//        }
//
//        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
//            override fun handleOnBackPressed() {
//
//                if (binding.getUpPressure.text.isNotEmpty() && binding.getDownPressure.text.isNotEmpty() && binding.getPulse.text.isNotEmpty()) {
//                    if (binding.getUpPressure.text.toString().toInt() in 30..220 && binding.getDownPressure.text.toString().toInt() in 30..220 && binding.getPulse.text.toString().toInt() in 30..220) {
//                        saveOrUpdateHeartBd()
//                    }
//                }
//                var navOptions = NavOptions.Builder()
//                    .setEnterAnim(androidx.navigation.ui.R.anim.nav_default_enter_anim)
//                    .setExitAnim(androidx.navigation.ui.R.anim.nav_default_exit_anim)
//                    .setPopEnterAnim(androidx.navigation.ui.R.anim.nav_default_pop_enter_anim)
//                    .setPopExitAnim(androidx.navigation.ui.R.anim.nav_default_pop_exit_anim)
//                    .setPopUpTo(R.id.mainFragment, true)
//                    .build()
//                //навигация и анимации
//                val direction = heartFragmentDirections.actionHeartFragmentToMainFragment()
//                navigation.navigate(direction, navOptions)
//            }
//        })
//
//    }
//
//    override fun onDestroyView() {
//        super.onDestroyView()
//    }
//
//    //Клавиатура убрана
//    private fun afterKeyboardIsDown(editText: EditText, key: String, id: Int, date : Long) {
//
//        if (editText.text.isNotEmpty() && editText.isFocused) {
//            //Сохранение
//            afterKeyboardClosedOrLostFocusForHeart(editText, key, id, date)
//
//        }
//
//    }
//
//    //Сохранение для главного экрана
//    private fun saveHeartSettings(heart : String) {
//
//        val editor = viewModel.settingsForHeart.edit()
//        editor?.putString(Constants.PRESSURE, heart)?.apply()
//
//    }
//
//    //Сохранение для sharedpref
//    private fun saveSharedPref(key : String, value : Int) {
//
//        val editor = viewModel.settingsForHeart.edit()
//        editor?.putInt(key, value)?.apply()
//
//    }
//
//    //Клавиатура закрыта ил потерян фокус
//    private fun afterKeyboardClosedOrLostFocusForHeart(editText: EditText, key: String, id: Int, date: Long) {
//
//        if (binding.getUpPressure.text.isNotEmpty() && binding.getDownPressure.text.isNotEmpty() && binding.getPulse.text.isNotEmpty()) {
//
//        if (binding.getUpPressure.text.toString().toInt() in 30..220 && binding.getDownPressure.text.toString().toInt() in 30..220 && binding.getPulse.text.toString().toInt() in 30..220) {
//
//            if (binding.getUpPressure.text.toString().toInt() != viewModel.settingsForHeart.getInt(Constants.UPPER, 0) ||
//                binding.getDownPressure.text.toString().toInt() != viewModel.settingsForHeart.getInt(Constants.LOWER, 0) ||
//                binding.getPulse.text.toString().toInt() != viewModel.settingsForHeart.getInt(Constants.PULSE, 0)) {
//
//                if (binding.getUpPressure.text.isNotEmpty() && binding.getDownPressure.text.isNotEmpty() && binding.getPulse.text.isNotEmpty() &&
//                    binding.getUpPressure.text.toString().toInt() in 30..220 && binding.getDownPressure.text.toString().toInt() in 30..220 && binding.getPulse.text.toString().toInt() in 30..220) {
//
//                    //Сохранение в бд
//                    saveOrUpdateHeartBdKeyBoard(id, date)
//
//                    viewModel.setCurrentId()
//                    viewModel.setCurrentDate()
//                    viewModel.setCurrentLowerPressure()
//                    viewModel.setCurrentUpperPressure()
//                    viewModel.setCurrentPulse()
//
//                    saveHeartSettings(binding.getUpPressure.text.toString() + "/" + binding.getDownPressure.text.toString())
//                    saveSharedPref(Constants.PULSE, binding.getPulse.text.toString().toInt())
//                    saveSharedPref(Constants.LOWER, binding.getDownPressure.text.toString().toInt())
//                    saveSharedPref(Constants.UPPER, binding.getUpPressure.text.toString().toInt())
//                }
//
//            }
//
//            editText.setSelection(editText.text.toString().length)
//
//            editText.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
//            binding.getPulse.setCompoundDrawablesWithIntrinsicBounds(0, 0,0, 0)
//            binding.getUpPressure.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
//            binding.getDownPressure.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
//
//        } else {
//            if (binding.getUpPressure.text.toString().toInt() !in 30..220) {
//                binding.getUpPressure.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.error_ic, 0)
//            }
//            else {
//                binding.getUpPressure.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
//            }
//            if (binding.getDownPressure.text.toString().toInt() !in 30..220) {
//                binding.getDownPressure.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.error_ic, 0)
//            }
//            else {
//                binding.getDownPressure.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
//            }
//            if (binding.getPulse.text.toString().toInt() !in 30..220) {
//                binding.getPulse.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.error_ic, 0)
//            }
//            else {
//                binding.getPulse.setCompoundDrawablesWithIntrinsicBounds(0, 0,0, 0)
//            }
//        }
//        }
//
//    }
//
//    //Дляя фокуса
//    private fun forFocus(boolean: Boolean) {
//        focusChange(binding.getUpPressure, boolean)
//        focusChange(binding.getDownPressure, boolean)
//        focusChange(binding.getPulse, boolean)
//    }
//
//    private fun focusChange(editText: EditText, boolean: Boolean) {
//        editText.isCursorVisible = boolean
//    }
//
//    private fun saveOrUpdateHeartBd() {
//
//        var id = 0
//        var date = 0L
//
//
//
//        lifecycleScope.launch {
//            viewModel.lastDate.collect {
//                if (it != null) {
//                    date = it
//                }
//            }
//        }
//
//        lifecycleScope.launch {
//            viewModel.lastId.collect {
//                if (it != null) {
//                    id = it
//                }
//            }
//        }
//
//        saveOrUpdateHeartBdKeyBoard(id, date)
//    }
//
//    private fun saveOrUpdateHeartBdKeyBoard(id: Int, date : Long) {
//
//        val currentDate = Calendar.getInstance().timeInMillis
//        Log.d("date", "current date: $currentDate day: $date id : $id ")
//
//        //Если новая дата не совпадает со старой -> insert
//        if (SimpleDateFormat("dd.MM").format(currentDate) != SimpleDateFormat("dd.MM").format(date)) {
//
//            if (binding.getPulse.text.isNotEmpty() && binding.getDownPressure.text.isNotEmpty() && binding.getUpPressure.text.isNotEmpty()) {
//
//                val ourHeart = forHeartBd(currentDate, 0)
//                Log.d("update", "saveOrUpdateHeartBdKeyBoard: insert $ourHeart")
//                viewModel.insertHeart(ourHeart)
//            }
//
//        } else { //Если новая дата совпадает со старой -> update
//
//            if (binding.getPulse.text.isNotEmpty() && binding.getDownPressure.text.isNotEmpty() && binding.getUpPressure.text.isNotEmpty()) {
//
//                val ourHeart = forHeartBd(currentDate, id)
//                Log.d("update", "saveOrUpdateHeartBdKeyBoard: update $ourHeart")
//                viewModel.updateHeart(ourHeart)
//
//            }
//
//        }
//
//    }
//
//    private fun forHeartBd(currentDate: Long, id: Int): Heart {
//        return Heart(
//            id = id,
//            pressureUp = binding.getUpPressure.text.toString().toInt(),
//            pressureDown = binding.getDownPressure.text.toString().toInt(),
//            pulse = binding.getPulse.text.toString().toInt(),
//            date = currentDate,
//        )
//    }

    }

}