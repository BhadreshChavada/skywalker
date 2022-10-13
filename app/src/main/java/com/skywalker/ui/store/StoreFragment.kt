package com.skywalker.ui.store

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.skywalker.R
import com.skywalker.connection.ResultWrapper
import com.skywalker.databinding.FragmentStoreBinding
import com.skywalker.helper.ApiProgressDialog
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class StoreFragment : Fragment(R.layout.fragment_store) {

    private lateinit var binding: FragmentStoreBinding

    private val storeViewModel: StoreViewModel by viewModels()
    private lateinit var mProgressDialog: ApiProgressDialog

    lateinit var countryAdapter: CountryAdapter
    lateinit var popularCountryAdapter: PopularCountryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_store, container, false
        )
        mProgressDialog = ApiProgressDialog(requireActivity())

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mProgressDialog.show()
        setRecycleView()
        setObserver()

    }

    private fun setRecycleView() {
        popularCountryAdapter = PopularCountryAdapter(requireActivity())
        binding.rvPopularCountry.adapter = popularCountryAdapter

        countryAdapter = CountryAdapter(requireActivity())
        binding.rvCountry.setLayoutManager(GridLayoutManager(requireActivity(), 4))
        binding.rvCountry.adapter = countryAdapter


    }

    private fun setObserver() {

        storeViewModel.countryLiveData.observe(viewLifecycleOwner) { result ->
            when (result) {
                is ResultWrapper.Success -> if (result.value != null) {
                    // Success code go here
                    mProgressDialog.dismiss()

                    countryAdapter.submitList(result.value.data)
                    popularCountryAdapter.submitList(result.value.data)
                    /* authenticationViewModel.signupRequest = SignupRequest("", "", "", "")
                     binding.tvLogin.performClick()
                     binding.viewModel = authenticationViewModel*/

                }
                is ResultWrapper.Error -> {
                    mProgressDialog.dismiss()
                }
                else -> {
                    mProgressDialog.dismiss()
                    // if result value is something else
                }
            }
        }


    }


}