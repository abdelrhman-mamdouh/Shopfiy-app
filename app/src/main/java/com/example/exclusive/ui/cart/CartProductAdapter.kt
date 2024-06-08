package com.example.exclusive.ui.cart

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.exclusive.databinding.ItemCartBinding
import com.example.exclusive.model.CartProduct
import com.example.exclusive.utilities.SnackbarUtils
import com.squareup.picasso.Picasso

private const val TAG = "CartProductAdapter"
class CartProductAdapter(private val listener: OnQuantityChangeListener) :
    ListAdapter<CartProduct, CartProductAdapter.CartProductViewHolder>(DIFF_CALLBACK) {

    private val selectedQuantities = mutableMapOf<String, Int>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartProductViewHolder {
        val binding = ItemCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartProductViewHolder(binding, listener)
    }

    override fun onBindViewHolder(holder: CartProductViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    fun addItem(position: Int, item: CartProduct) {
        val currentList = currentList.toMutableList()
        currentList.add(position, item)
        submitList(currentList)
    }

    fun removeItem(position: Int): CartProduct? {
        val currentList = currentList.toMutableList()
        val removedItem = currentList.removeAt(position)
        submitList(currentList)
        return removedItem
    }

    fun getCurrentQuantity(productId: String): Int {
        return selectedQuantities[productId] ?: 1
    }

    inner class CartProductViewHolder(
        private val binding: ItemCartBinding,
        private val listener: OnQuantityChangeListener
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(product: CartProduct) {
            Log.d(TAG, "bind: ${product.id} ${product.productId} ${product.variantId}")
            binding.etTitle.text = product.productTitle
            binding.textViewProductPrice.text = product.variantPrice +" "+ product.variantPriceCode
            binding.tvVariant.text = "${product.quantity} Items"
            Picasso.get().load(product.productImageUrl).into(binding.cartProductImageView)
            val initialQuantity = selectedQuantities[product.id] ?: 1
            binding.tvQuantity.text = initialQuantity.toString()
            binding.btnIncrease.setOnClickListener {
                val currentQuantity = binding.tvQuantity.text.toString().toInt()
                if (currentQuantity < product.quantity) {
                    val newQuantity = currentQuantity + 1
                    binding.tvQuantity.text = newQuantity.toString()
                    selectedQuantities[product.id] = newQuantity
                    listener.onQuantityChanged()
                } else {
                    SnackbarUtils.showSnackbar(binding.root.context, binding.root, "Out of stock")
                }
            }

            binding.btnDecrease.setOnClickListener {
                val currentQuantity = binding.tvQuantity.text.toString().toInt()
                if (currentQuantity > 1) {
                    val newQuantity = currentQuantity - 1
                    binding.tvQuantity.text = newQuantity.toString()
                    selectedQuantities[product.id] = newQuantity
                    listener.onQuantityChanged()
                } else {
                    listener.onRemoveProduct(product)
                }
            }
        }
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

    interface OnQuantityChangeListener {
        fun onRemoveProduct(product: CartProduct)
        fun onQuantityChanged()
    }

}