package com.example.healthcheck.ui.start

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.healthcheck.R
import com.example.healthcheck.databinding.FragmentStartBinding
import com.example.healthcheck.util.animations.buttonChangeScreenAnimation.buttonChangeScreenAnimation
import com.example.healthcheck.viewmodels.start.StartViewModel
import com.example.healthcheck.viewmodels.main.ViewPagerAdapter

class StartFragment : Fragment() {

    companion object {
        fun newInstance() = StartFragment()
    }

    private lateinit var viewModel: StartViewModel
    private lateinit var binding : FragmentStartBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStartBinding.inflate(inflater)
        viewModel = ViewModelProvider(this).get(StartViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var navOptions = NavOptions.Builder()
            .setEnterAnim(androidx.navigation.ui.R.anim.nav_default_enter_anim)
            .setExitAnim(androidx.navigation.ui.R.anim.nav_default_exit_anim)
            .setPopEnterAnim(androidx.navigation.ui.R.anim.nav_default_pop_enter_anim)
            .setPopExitAnim(androidx.navigation.ui.R.anim.nav_default_pop_exit_anim)
            .build()

        var currentPage = 0

        val viewPagerAdapter = ViewPagerAdapter(this, viewModel.fragList)
        binding.viewPager.adapter = viewPagerAdapter

        val navigation = findNavController()

        binding.skipStart.setOnClickListener {
            val direction = StartFragmentDirections.actionStartFragmentToStartFragment4()
            val navigate = { nav : NavController, d : NavDirections, n : NavOptions -> nav.navigate(d, n)}
            buttonChangeScreenAnimation(binding.skipStart, navigation, direction, navOptions, navigate)
        }

        binding.next.setOnClickListener {
            movePagesViewPager(++currentPage)
            if (currentPage > 3) {
                navigation.navigate(R.id.startFragment4)
            }
        }

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                scrollViewPager(position)
                currentPage = position+1
            }
        })

        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {

            }
        })

    }

    private fun movePagesViewPager(position : Int) {
        when(position) {
            1 -> {
                binding.viewPager.currentItem = 0
                scrollViewPager(0)
            }
            2 -> {
                binding.viewPager.currentItem = 1
                scrollViewPager(1)
            }
            3 -> {
                binding.viewPager.currentItem = 2
                scrollViewPager(2)
            }
        }
    }

    private fun scrollViewPager(position : Int) {
        when (position) {
            0 -> {
                setChoosenBtn(binding.circle1)
                setUnchoosenBtn(binding.circle2)
                setUnchoosenBtn(binding.circle3)
            }
            1 -> {
                setUnchoosenBtn(binding.circle1)
                setChoosenBtn(binding.circle2)
                setUnchoosenBtn(binding.circle3)
            }
            2 -> {
                setUnchoosenBtn(binding.circle1)
                setUnchoosenBtn(binding.circle2)
                setChoosenBtn(binding.circle3)
            }
        }
    }

    private fun setChoosenBtn(view : ImageView) {
        view.setImageResource(R.drawable.grey_circle)
    }

    private fun setUnchoosenBtn(view : ImageView) {
        view.setImageResource(R.drawable.white_circle)
    }

}