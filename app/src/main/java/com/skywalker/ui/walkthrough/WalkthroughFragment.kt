package com.skywalker.ui.walkthrough

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.skywalker.R
import com.skywalker.databinding.FragmentWalkthroughBinding
import com.skywalker.ui.walkthrough.WalkthroughAdapter


class WalkthroughFragment : Fragment(R.layout.fragment_walkthrough) {

    private lateinit var binding: FragmentWalkthroughBinding

    var mViewPagerAdapter: WalkthroughAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_walkthrough, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setWtData()
        setListener()
    }

    private fun setListener() {
        binding.tvSkip.setOnClickListener {
            findNavController().navigate(R.id.action_walkthroughFragment_to_authenticationFragment)
        }

        binding.btnNextWt.setOnClickListener {
            if (binding.wtPager.currentItem == mViewPagerAdapter?.count!!-1) {
                findNavController().navigate(R.id.action_walkthroughFragment_to_authenticationFragment)
            } else {
                binding.wtPager.setCurrentItem(binding.wtPager.currentItem + 1)
            }
        }
    }

    private fun setWtData() {
        val imageArray = intArrayOf(
            R.drawable.ic_wt_one,
            R.drawable.ic_wt_two,
            R.drawable.ic_wt_three
        )
        val stringArray = arrayOf(
            getString(R.string.wt_one), getString(R.string.wt_two),
            getString(R.string.wt_three)
        )


        mViewPagerAdapter = WalkthroughAdapter(requireActivity(), imageArray, stringArray)
        binding.wtPager.setAdapter(mViewPagerAdapter)


        binding.tabLayout.setupWithViewPager(binding.wtPager, true)
    }

}