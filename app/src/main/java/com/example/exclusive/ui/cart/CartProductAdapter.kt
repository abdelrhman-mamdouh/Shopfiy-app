package com.example.exclusive.ui.cart

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.exclusive.databinding.ItemCartBinding
import com.example.exclusive.model.CartProduct
import com.squareup.picasso.Picasso

class CartProductAdapter : ListAdapter<CartProduct, CartProductAdapter.CartProductViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartProductViewHolder {
        val binding = ItemCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartProductViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class CartProductViewHolder(private val binding: ItemCartBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(product: CartProduct) {
            binding.etTitle.text = product.productTitle
            binding.textViewProductPrice.text = product.variantPrice
            binding.tvVariant.text = "${product.quantity} Items"

            Picasso.get().load(product.productImageUrl).into(binding.cartProductImageView)

            binding.btnIncrease.setOnClickListener {
                // Handle increase quantity button click
                // You may want to notify the listener about the quantity change
            }
            binding.btnDecrease.setOnClickListener {

            }
        }
    }
    fun removeItem(position: Int): CartProduct? {
        val currentList = currentList.toMutableList()
        val removedItem = currentList.removeAt(position)
        submitList(currentList)
        return removedItem
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<CartProduct>() {
            override fun areItemsTheSame(oldItem: CartProduct, newItem: CartProduct): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: CartProduct, newItem: CartProduct): Boolean {
                return oldItem == newItem
            }
        }
    }
}