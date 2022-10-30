package com.skywalker.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.skywalker.R
import com.skywalker.databinding.FragmentAboutSkywalkerBinding
import com.skywalker.databinding.FragmentMoreInfoBinding

class AboutSkywalkerFragment : Fragment(R.layout.fragment_about_skywalker) {

    private lateinit var binding: FragmentAboutSkywalkerBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_about_skywalker, container, false
        )
        binding.toolbar.tvTitle.text = getString(R.string.about_skywalker)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setClickListener()
    }

    private fun setClickListener() {
        binding.toolbar.ivBack.setOnClickListener { requireActivity().onBackPressed() }
    }

}