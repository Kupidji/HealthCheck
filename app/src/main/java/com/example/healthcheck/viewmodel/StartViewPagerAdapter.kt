package com.example.healthcheck.viewmodel

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class StartViewPagerAdapter (fragment : Fragment, val list : List<Fragment>) : FragmentStateAdapter(fragment)  {

    override fun getItemCount(): Int {
        return list.size
    }

    override fun createFragment(position: Int): Fragment {
        return list[position]
    }

}