package com.app.myworkoutapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.app.myworkoutapp.data.ActivityMC
import com.app.myworkoutapp.databinding.ListItemBinding


class ActivitiesListAdapter(private val onItemClicked: (ActivityMC) -> Unit) :
    ListAdapter<ActivityMC, ActivitiesListAdapter.ItemViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            ListItemBinding.inflate(
                LayoutInflater.from(
                    parent.context
                )
            )
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val current = getItem(position)
        holder.itemView.setOnClickListener {
            onItemClicked(current)
        }
        holder.bind(current)
    }

    class ItemViewHolder(private var binding: ListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ActivityMC) {
            binding.titleListItem.text = item.title
            binding.placeListItem.text = item.place
            binding.dateListItem.text = item.date
        }
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<ActivityMC>() {
            override fun areItemsTheSame(oldItem: ActivityMC, newItem: ActivityMC): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: ActivityMC, newItem: ActivityMC): Boolean {
                return oldItem.title == newItem.title
            }
        }
    }
}
