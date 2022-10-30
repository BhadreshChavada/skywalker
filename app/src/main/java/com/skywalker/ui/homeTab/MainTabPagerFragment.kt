package com.skywalker.ui.homeTab

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.skywalker.R
import com.skywalker.databinding.FragmentMainTabPagerBinding
import com.skywalker.ui.profile.ProfileFragment
import com.skywalker.ui.store.StoreFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainTabPagerFragment : Fragment(R.layout.fragment_main_tab_pager) {

    private lateinit var binding: FragmentMainTabPagerBinding

    lateinit var tabsPagerAdapter: TabsPagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_main_tab_pager, container, false
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tabsPagerAdapter =
            TabsPagerAdapter(
                childFragmentManager, viewLifecycleOwner.lifecycle
            )
        binding.pager.adapter = tabsPagerAdapter
        binding.pager.isUserInputEnabled = true
        setupTabMediator()
    }

    private fun setupTabMediator() {
        val storeFragment = StoreFragment()
        val myESimFragment = MyESimFragment()
        val profileFragment = ProfileFragment()


        val tabs: MutableList<Drawable> = mutableListOf()
        tabs.add(0, ContextCompat.getDrawable(requireActivity(), R.drawable.ic_tab_home_selector)!!)
        tabs.add(
            1,
            ContextCompat.getDrawable(requireActivity(), R.drawable.ic_tab_my_esim_selector)!!
        )
        tabs.add(
            2,
            ContextCompat.getDrawable(requireActivity(), R.drawable.ic_tab_profile_selector)!!
        )

        TabLayoutMediator(
            binding.tabLayout,
            binding.pager
        ) { tab: TabLayout.Tab, position: Int ->
            val view: View = LayoutInflater.from(activity)
                .inflate(R.layout.layout_home_tab_icon, null)
            view.findViewById<ImageView>(R.id.ivTabIcon).setImageDrawable(tabs[position])
            tab.customView = view
        }.attach()

        tabsPagerAdapter.addFragment(0, storeFragment)
        tabsPagerAdapter.addFragment(1, myESimFragment)
        tabsPagerAdapter.addFragment(2, profileFragment)
        tabsPagerAdapter.notifyDataSetChanged()
    }
}