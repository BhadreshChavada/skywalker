package com.skywalker.ui.mySim

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.skywalker.R
import com.skywalker.databinding.AdapterMySimCardBinding
import com.skywalker.databinding.AdapterSimCardBinding
import com.skywalker.helper.Utils.getCardThemeBackground
import com.skywalker.helper.Utils.getCardThemeImage
import com.skywalker.model.respone.PlanDataItem

class ActivatedSimAdapter(val context: Context, private val planAdapterItemClick: PlanAdapterItemClick) :
    ListAdapter<PlanDataItem, ActivatedSimAdapter.OptionsViewHolder>(OPTIONS_DIFF_UTIL) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OptionsViewHolder {
        return OptionsViewHolder(parent, context, planAdapterItemClick)
    }

    override fun onBindViewHolder(holder: OptionsViewHolder, position: Int) {
        holder.bindTo(getItem(position))
    }


    class OptionsViewHolder(
        private val parent: ViewGroup,
        private val context: Context,
        private val planAdapterItemClick: PlanAdapterItemClick
    ) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.adapter_my_sim_card, parent, false)
    ) {

        private val binding = AdapterMySimCardBinding.bind(itemView)


        fun bindTo(item: PlanDataItem) {
            binding.apply {
                data = item
                clMain.setOnClickListener {
                    planAdapterItemClick.redirectToDetails(item)
                }
                btnRegister.setOnClickListener {
                    planAdapterItemClick.redirectToDetails(item)
                }
            }

            binding.bgCard.setImageDrawable(getCardThemeBackground(item.theme,context))
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

    interface PlanAdapterItemClick {
        fun redirectToDetails(planDataItem: PlanDataItem)
    }
}