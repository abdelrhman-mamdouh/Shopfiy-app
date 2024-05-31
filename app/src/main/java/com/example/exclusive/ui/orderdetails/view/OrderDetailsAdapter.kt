package com.example.exclusive.ui.orderdetails.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.exclusive.R
import com.example.exclusive.databinding.RowOrderProductBinding
import com.example.exclusive.model.ProductItem



class OrderDetailsAdapter(
    private val productList: List<ProductItem>
) : RecyclerView.Adapter<OrderDetailsAdapter.OrderViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val binding =
            RowOrderProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OrderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        holder.bind(productList[position])
    }

    override fun getItemCount() = productList.size

    inner class OrderViewHolder(private val binding: RowOrderProductBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(productItem: ProductItem) {
            binding.apply {
                titleTextView.text = productItem.title
                colorTextView.text = productItem.color
                sizeTextView.text = productItem.size
                unitsTextView.text = productItem.units
                priceTextView.text = productItem.price
                Glide.with(imageView.context)
                    .load(productItem.imageUrl)
                    .placeholder(R.drawable.ic_shopping_cart)
                    .into(imageView)
            }
        }
    }
}
