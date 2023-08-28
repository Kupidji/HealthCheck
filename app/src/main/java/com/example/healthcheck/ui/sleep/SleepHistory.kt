package com.example.healthcheck.ui.sleep

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.domain.AppDispatchers
import com.example.domain.models.Sleep
import com.example.healthcheck.R
import com.example.healthcheck.databinding.FragmentSleepHistoryBinding
import com.example.healthcheck.util.animations.buttonChangeScreenAnimation.buttonChangeScreenAnimation
import com.example.healthcheck.viewmodels.sleep.SleepActionListener
import com.example.healthcheck.viewmodels.sleep.SleepHistoryRecyclerViewAdapter
import com.example.healthcheck.viewmodels.sleep.SleepHistoryViewModel
import kotlinx.coroutines.launch

class SleepHistory : Fragment() {

    companion object {
        fun newInstance() = SleepHistory()
    }

    private lateinit var viewModel : SleepHistoryViewModel
    private lateinit var binding : FragmentSleepHistoryBinding
    private lateinit var adapter : SleepHistoryRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSleepHistoryBinding.inflate(inflater)
        viewModel = ViewModelProvider(this).get(SleepHistoryViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navigation = findNavController()

        val layoutManager = LinearLayoutManager(this.requireContext())
        adapter = SleepHistoryRecyclerViewAdapter(object : SleepActionListener {
            override fun onBoxClickAction(sleep: Sleep) {
                TODO("Not yet implemented")
            }

        })

        lifecycleScope.launch(AppDispatchers.main) {
            binding.sleepHistoryRecyclerView.adapter = adapter
            binding.sleepHistoryRecyclerView.layoutManager = layoutManager
            viewModel.sleepListHistory.collect { list ->
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
                .setPopUpTo(R.id.sleepFragment, true)
                .build()
            //навигация и анимации
            val direction = SleepHistoryDirections.actionSleepHistoryToSleepFragment()
            val navigate = { nav: NavController, d: NavDirections, n: NavOptions -> nav.navigate(d, n) }
            buttonChangeScreenAnimation(binding.wentBack, navigation, direction, navOptions, navigate)
        }

    }

}