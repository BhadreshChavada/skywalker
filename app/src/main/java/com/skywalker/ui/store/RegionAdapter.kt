package com.skywalker.ui.store

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.skywalker.R
import com.skywalker.databinding.AdapterRegionBinding
import com.skywalker.helper.AdapterItemClickListener
import com.skywalker.model.respone.CountryDataItem
import com.skywalker.model.respone.RegionDataItem

class RegionAdapter(
    val context: Context,
    private val adapterItemClickListener: AdapterItemClickListener<RegionDataItem>
) :
    ListAdapter<RegionDataItem, RegionAdapter.OptionsViewHolder>(OPTIONS_DIFF_UTIL) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OptionsViewHolder {
        return OptionsViewHolder(parent, context,adapterItemClickListener)
    }

    override fun onBindViewHolder(holder: OptionsViewHolder, position: Int) {
        holder.bindTo(getItem(position))
    }


    class OptionsViewHolder(
        private val parent: ViewGroup,
        private val context: Context,
        private val adapterItemClickListener: AdapterItemClickListener<RegionDataItem>
    ) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.adapter_region, parent, false)
    ) {

        private val binding = AdapterRegionBinding.bind(itemView)

        fun bindTo(item: RegionDataItem) {
            binding.apply {
                data = item

            }
            binding.root.setOnClickListener {
                adapterItemClickListener.itemClicked(item)
            }
        }
    }

    companion object {
        private val OPTIONS_DIFF_UTIL = object : DiffUtil.ItemCallback<RegionDataItem>() {
            override fun areItemsTheSame(
                oldItem: RegionDataItem,
                newItem: RegionDataItem
            ): Boolean {
                return oldItem.regionId == newItem.regionId
            }

            override fun areContentsTheSame(
                oldItem: RegionDataItem,
                newItem: RegionDataItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}