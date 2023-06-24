package com.example.healthcheck.view

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavOptions
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.example.healthcheck.R
import com.example.healthcheck.databinding.FragmentHeartBinding
import com.example.healthcheck.model.heart.entities.Heart
import com.example.healthcheck.util.Constants
import com.example.healthcheck.util.animations.buttonChangeScreenAnimation.buttonChangeScreenAnimation
import com.example.healthcheck.viewmodel.HeartViewModel
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

class heartFragment : Fragment() {

    companion object {
        fun newInstance() = heartFragment()
    }

    private lateinit var viewModel: HeartViewModel
    private lateinit var binding : FragmentHeartBinding

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
        val tripletsPool = ThreadPoolExecutor(3, 3, 5L, TimeUnit.SECONDS, LinkedBlockingQueue())
        val navigation = findNavController()

        var navOptions = NavOptions.Builder()
            .setEnterAnim(androidx.navigation.ui.R.anim.nav_default_enter_anim)
            .setExitAnim(androidx.navigation.ui.R.anim.nav_default_exit_anim)
            .setPopEnterAnim(androidx.navigation.ui.R.anim.nav_default_pop_enter_anim)
            .setPopExitAnim(androidx.navigation.ui.R.anim.nav_default_pop_exit_anim)
            .build()

        GlobalScope.launch(Dispatchers.Main, CoroutineStart.DEFAULT) {
            binding.ur1.setText(viewModel.getCardioFromDataUpPressure(tripletsPool).toString())
        }

        GlobalScope.launch(Dispatchers.Main, CoroutineStart.DEFAULT) {
            binding.ur2.setText(viewModel.getCardioFromDataDownPressure(tripletsPool).toString())
        }

        GlobalScope.launch(Dispatchers.Main, CoroutineStart.DEFAULT) {
            binding.ur3.setText(viewModel.getCardioFromDataPulse(tripletsPool).toString())
        }

        binding.wentBack.setOnClickListener {
            if (binding.getUpPressure.text.isNotEmpty() && binding.getDownPressure.text.isNotEmpty() && binding.getPulse.text.isNotEmpty()) {
                var currentTime = Calendar.getInstance().timeInMillis
                saveHeartSettings(binding.getUpPressure.text.toString() + "/" + binding.getDownPressure.text.toString())
                var heart = Heart(
                    binding.getUpPressure.text.toString().toInt(),
                    binding.getDownPressure.text.toString().toInt(),
                    binding.getPulse.text.toString().toInt(),
                    currentTime,
                )
                viewModel.insertHeart(heart)
            }
            //навигация и анимации
            val direction = heartFragmentDirections.actionHeartFragmentToMainFragment()
            val navigate = { nav : NavController, d : NavDirections, n : NavOptions -> nav.navigate(d, n)}
            buttonChangeScreenAnimation(binding.wentBack, navigation, direction, navOptions, navigate)
        }

        //навигация и анимации
        binding.profile.setOnClickListener {
            val direction = heartFragmentDirections.actionHeartFragmentToProfileFragment()
            val navigate = { nav : NavController, d : NavDirections, n : NavOptions -> nav.navigate(d, n)}
            buttonChangeScreenAnimation(binding.profile, navigation, direction, navOptions, navigate)
        }

    }

    private fun saveHeartSettings(heart : String) {

        val editor = viewModel.settings.edit()
        editor?.putString(
            Constants.PRESSURE,
            heart
        )?.apply()

    }

}