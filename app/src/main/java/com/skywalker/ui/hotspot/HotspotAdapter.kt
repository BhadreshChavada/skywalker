package com.skywalker.ui.hotspot

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.skywalker.R
import com.skywalker.databinding.AdapterHotspotBinding
import com.skywalker.helper.Utils.getHotSpotThemeBackground
import com.skywalker.model.respone.HotspotDetails

class HotspotAdapter(
    val context: Context,
    private val itemClick: HotSpotItemClick
) :
    ListAdapter<HotspotDetails, HotspotAdapter.OptionsViewHolder>(OPTIONS_DIFF_UTIL) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OptionsViewHolder {
        return OptionsViewHolder(parent, context, itemClick)
    }

    override fun onBindViewHolder(holder: OptionsViewHolder, position: Int) {
        holder.bindTo(getItem(position))
    }


    class OptionsViewHolder(
        private val parent: ViewGroup,
        private val context: Context,
        private val itemClick: HotSpotItemClick
    ) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.adapter_hotspot, parent, false)
    ) {

        private val binding = AdapterHotspotBinding.bind(itemView)


        fun bindTo(item: HotspotDetails) {
            binding.apply {
                data = item

                root.setOnClickListener {
                    itemClick.redirectToDetails(item)
                }
            }

            binding.bgCard.setImageDrawable(getHotSpotThemeBackground(item.theme, context))
        }


    }

    companion object {
        private val OPTIONS_DIFF_UTIL = object : DiffUtil.ItemCallback<HotspotDetails>() {
            override fun areItemsTheSame(
                oldItem: HotspotDetails,
                newItem: HotspotDetails
            ): Boolean {
                return oldItem.title == newItem.title
            }

            override fun areContentsTheSame(
                oldItem: HotspotDetails,
                newItem: HotspotDetails
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    interface HotSpotItemClick {
        fun redirectToDetails(planDataItem: HotspotDetails)
    }
}