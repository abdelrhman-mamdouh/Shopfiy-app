package com.example.exclusive.ui.checkout.view


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.exclusive.databinding.ItemAddressBinding
import com.example.exclusive.data.model.AddressInput


class AddressAdapter(
    private val addresses: List<AddressInput>,
    private val onAddressSelected: (AddressInput) -> Unit
) : RecyclerView.Adapter<AddressAdapter.AddressViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressViewHolder {
        val binding = ItemAddressBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AddressViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AddressViewHolder, position: Int) {
        val address = addresses[position]
        holder.bind(address)
    }

    override fun getItemCount(): Int = addresses.size

    inner class AddressViewHolder(private val binding: ItemAddressBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(address: AddressInput) {
            binding.tvStreetName.text = "${address.address1}, ${address.city}, ${address.country}"
            binding.tvPhone.text = address.phone
            binding.root.setOnClickListener {
                onAddressSelected(address)
            }
        }
    }
}
