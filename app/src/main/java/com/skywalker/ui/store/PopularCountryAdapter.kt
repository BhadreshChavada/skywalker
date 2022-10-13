package com.skywalker.ui.store

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.skywalker.R
import com.skywalker.databinding.AdapterPopularCountryBinding
import com.skywalker.model.respone.CountryDataItem

class PopularCountryAdapter(val context: Context) :
    ListAdapter<CountryDataItem, PopularCountryAdapter.OptionsViewHolder>(OPTIONS_DIFF_UTIL) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OptionsViewHolder {
        return OptionsViewHolder(parent, context)
    }

    override fun onBindViewHolder(holder: OptionsViewHolder, position: Int) {
        holder.bindTo(getItem(position))
    }


    class OptionsViewHolder(
        private val parent: ViewGroup,
        private val context: Context
    ) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.adapter_popular_country, parent, false)
    ) {

        private val binding = AdapterPopularCountryBinding.bind(itemView)

        fun bindTo(item: CountryDataItem) {
            binding.apply {
                data = item

            }
        }
    }

    companion object {
        private val OPTIONS_DIFF_UTIL = object : DiffUtil.ItemCallback<CountryDataItem>() {
            override fun areItemsTheSame(
                oldItem: CountryDataItem,
                newItem: CountryDataItem
            ): Boolean {
                return oldItem.countryId == newItem.countryId
            }

            override fun areContentsTheSame(
                oldItem: CountryDataItem,
                newItem: CountryDataItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}