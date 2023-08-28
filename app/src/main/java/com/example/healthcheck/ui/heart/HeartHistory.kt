package com.example.healthcheck.ui.heart

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
import com.example.healthcheck.R
import com.example.healthcheck.databinding.FragmentHeartHistoryBinding
import com.example.healthcheck.util.animations.buttonChangeScreenAnimation.buttonChangeScreenAnimation
import com.example.healthcheck.viewmodels.heart.HeartHistoryViewModel
import com.example.healthcheck.viewmodels.heart.HeartRecyclerViewAdapter
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
        adapter = HeartRecyclerViewAdapter()

        lifecycleScope.launch(AppDispatchers.main) {
            binding.heartHistoryRecyclerView.layoutManager = layoutManager
            binding.heartHistoryRecyclerView.adapter = adapter
            viewModel.historyHeartList.collect { list ->
                adapter.cardioList = list
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

}