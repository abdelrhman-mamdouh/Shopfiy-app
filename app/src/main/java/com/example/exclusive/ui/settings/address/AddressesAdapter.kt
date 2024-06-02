package com.example.exclusive.ui.settings.address

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.exclusive.databinding.ItemAddressBinding
import com.example.exclusive.model.AddressInput

class AddressesAdapter : ListAdapter<AddressInput, AddressesAdapter.AddressViewHolder>(AddressDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressViewHolder {
        val binding = ItemAddressBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AddressViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AddressViewHolder, position: Int) {
        val address = getItem(position)
        holder.bind(address)
    }

    inner class AddressViewHolder(private val binding: ItemAddressBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(address: AddressInput) {
            binding.tvName.text = address.firstName
            binding.tvStreetName.text = "${address.address1}, ${address.city}, ${address.country}"
            binding.tvPhone.text = address.phone
            // Set other fields if necessary
        }
    }

    class AddressDiffCallback : DiffUtil.ItemCallback<AddressInput>() {
        override fun areItemsTheSame(oldItem: AddressInput, newItem: AddressInput): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: AddressInput, newItem: AddressInput): Boolean {
            return oldItem == newItem
        }
    }
}