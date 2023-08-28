package com.example.healthcheck.ui.weight

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.example.healthcheck.databinding.FragmentWeightHistoryBinding
import com.example.healthcheck.util.animations.buttonChangeScreenAnimation.buttonChangeScreenAnimation
import com.example.healthcheck.viewmodels.weight.WeightActionsListener
import com.example.healthcheck.viewmodels.weight.WeightHistoryRecyclerViewAdapter
import com.example.healthcheck.viewmodels.weight.WeightHistoryViewModel
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
                TODO("Not yet implemented")
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

}