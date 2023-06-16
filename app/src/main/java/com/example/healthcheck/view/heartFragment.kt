package com.example.healthcheck.view

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.healthcheck.R
import com.example.healthcheck.databinding.FragmentHeartBinding
import com.example.healthcheck.model.heart.entities.Heart
import com.example.healthcheck.viewmodel.HeartViewModel
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar

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
        val navigation = findNavController()

        lifecycleScope.launch(Dispatchers.Main, CoroutineStart.DEFAULT) {
            binding.ur1.setText(viewModel.list[0].toString())
            binding.ur2.setText(viewModel.list[1].toString())
            binding.ur3.setText(viewModel.list[2].toString())
        }

        binding.wentBack.setOnClickListener {
            if (binding.getUpPressure.text.isNotEmpty() && binding.getDownPressure.text.isNotEmpty() && binding.getPulse.text.isNotEmpty()) {
                var currentTime = Calendar.getInstance().timeInMillis
                var heart = Heart(
                    binding.getUpPressure.text.toString().toInt(),
                    binding.getDownPressure.text.toString().toInt(),
                    binding.getPulse.text.toString().toInt(),
                    currentTime,
                )
                viewModel.insertHeart(heart)
            }
            navigation.navigate(R.id.mainFragment)
        }

        binding.profile.setOnClickListener {
            navigation.navigate(R.id.profileFragment)
        }

    }

}