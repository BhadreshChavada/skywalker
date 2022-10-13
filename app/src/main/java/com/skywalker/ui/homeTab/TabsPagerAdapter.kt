package com.skywalker.ui.homeTab

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class TabsPagerAdapter(fragment: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragment, lifecycle) {

    private val tabFragmentsCreators: MutableMap<Int, Fragment> = mutableMapOf()

    override fun getItemCount() = tabFragmentsCreators.size

    override fun createFragment(position: Int): Fragment {
        return tabFragmentsCreators[position]!!
    }

    fun addFragment(position: Int, fragment: Fragment) {
        tabFragmentsCreators[position] = fragment
    }

}