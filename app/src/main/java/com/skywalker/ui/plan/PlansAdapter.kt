package com.skywalker.ui.plan

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.skywalker.R
import com.skywalker.databinding.AdapterSimCardBinding
import com.skywalker.model.respone.PlanDataItem

class PlansAdapter(val context: Context) :
    ListAdapter<PlanDataItem, PlansAdapter.OptionsViewHolder>(OPTIONS_DIFF_UTIL) {

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
        LayoutInflater.from(parent.context).inflate(R.layout.adapter_sim_card, parent, false)
    ) {

        private val binding = AdapterSimCardBinding.bind(itemView)

        fun bindTo(item: PlanDataItem) {
            binding.apply {
                data = item
            }

            when (item.theme) {
                0 -> {
                    binding.bgCard.setImageDrawable(context.resources.getDrawable(R.drawable.bg_card_1))
                    binding.ivCard.setImageDrawable(context.resources.getDrawable(R.drawable.iv_card_1))
                }
                1 -> {
                    binding.bgCard.setImageDrawable(context.resources.getDrawable(R.drawable.bg_card_2))
                    binding.ivCard.setImageDrawable(context.resources.getDrawable(R.drawable.iv_card_2))
                }
                else -> {
                    binding.bgCard.setImageDrawable(context.resources.getDrawable(R.drawable.bg_card_3))
                    binding.ivCard.setImageDrawable(context.resources.getDrawable(R.drawable.iv_card_3))
                }
            }
        }
    }

    companion object {
        private val OPTIONS_DIFF_UTIL = object : DiffUtil.ItemCallback<PlanDataItem>() {
            override fun areItemsTheSame(
                oldItem: PlanDataItem,
                newItem: PlanDataItem
            ): Boolean {
                return oldItem.planId == newItem.planId
            }

            override fun areContentsTheSame(
                oldItem: PlanDataItem,
                newItem: PlanDataItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}