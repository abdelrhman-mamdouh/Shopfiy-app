package com.example.exclusive.ui.settings.address

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.exclusive.databinding.ItemAddressBinding
import com.example.exclusive.model.AddressInput
import com.example.exclusive.model.CartProduct

class AddressesAdapter(private val onItemClick: (AddressInput) -> Unit) :
    ListAdapter<AddressInput, AddressesAdapter.AddressViewHolder>(AddressDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressViewHolder {
        val binding = ItemAddressBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AddressViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AddressViewHolder, position: Int) {
        val address = getItem(position)
        holder.bind(address)
    }

    inner class AddressViewHolder(private val binding: ItemAddressBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClick(getItem(position))
                }
            }
        }

        fun bind(address: AddressInput) {
            binding.tvName.text = address.firstName
            binding.tvStreetName.text = "${address.address1}, ${address.city}, ${address.country}"
            binding.tvPhone.text = address.phone
        }
    }

    fun removeItem(position: Int): AddressInput? {
        val currentList = currentList.toMutableList()
        val removedItem = currentList.removeAt(position)
        submitList(currentList)
        return removedItem
    }

    fun addItem(position: Int, address: AddressInput) {
        val currentList = currentList.toMutableList()
        currentList.add(position, address)
        submitList(currentList)
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
