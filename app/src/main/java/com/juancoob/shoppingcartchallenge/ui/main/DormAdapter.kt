package com.juancoob.shoppingcartchallenge.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.juancoob.domain.Dorm
import com.juancoob.shoppingcartchallenge.databinding.DormItemBinding

class DormAdapter(private val listener: (Dorm) -> Unit) :
    ListAdapter<Dorm, DormAdapter.ViewHolder>(object : DiffUtil.ItemCallback<Dorm>() {
        override fun areItemsTheSame(oldItem: Dorm, newItem: Dorm): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Dorm, newItem: Dorm): Boolean =
            oldItem == newItem
    }) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DormItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dorm = getItem(position)
        holder.bind(dorm)
        holder.itemView.setOnClickListener { listener(dorm) }
    }

    inner class ViewHolder(private val binding: DormItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(dorm: Dorm) = binding.run {
            type.text = dorm.type
            bedsAvailable.text = dorm.bedsAvailable.toString()
        }
    }
}
