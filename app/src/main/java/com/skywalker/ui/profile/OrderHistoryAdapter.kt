package com.skywalker.ui.profile

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.skywalker.R
import com.skywalker.databinding.AdapterOrderHistoryBinding
import com.skywalker.helper.Utils.getCardThemeBackground
import com.skywalker.helper.Utils.getDateTime
import com.skywalker.model.respone.PlanDataItem

class OrderHistoryAdapter(val context: Context, private val orderAdapterItemClick: ItemClick) :
    ListAdapter<PlanDataItem, OrderHistoryAdapter.OptionsViewHolder>(OPTIONS_DIFF_UTIL) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OptionsViewHolder {
        return OptionsViewHolder(parent, context)
    }

    override fun onBindViewHolder(holder: OptionsViewHolder, position: Int) {
        holder.bindTo(getItem(position),orderAdapterItemClick)
    }


    class OptionsViewHolder(
        private val parent: ViewGroup,
        private val context: Context
    ) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.adapter_order_history, parent, false)
    ) {

        private val binding = AdapterOrderHistoryBinding.bind(itemView)


        fun bindTo(item: PlanDataItem, orderAdapterItemClick: ItemClick) {
            binding.apply {
                data = item
                tvDate.text = item.createdAt.toLong().getDateTime()
            }

            binding.root.setOnClickListener {
                orderAdapterItemClick.redirectToDetails(item)
            }

            binding.bgCard.setImageDrawable(getCardThemeBackground(item.theme, context))
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

    interface ItemClick {
        fun redirectToDetails(planDataItem: PlanDataItem)
    }

}