package com.example.exclusive.ui.orders.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.exclusive.databinding.ItemOrderBinding
import com.example.exclusive.data.model.MyOrder
import java.text.SimpleDateFormat
import java.util.Locale


class OrderAdapter(
    private val orderList: List<MyOrder>, private val listener: OnOrderClickListener
) : RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val binding = ItemOrderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OrderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        holder.bind(orderList[position])
    }

    override fun getItemCount() = orderList.size

    inner class OrderViewHolder(private val binding: ItemOrderBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener.onOrderClick(orderList[position])
                }
            }
        }

        fun bind(orderItem: MyOrder) {
            binding.apply {
                tvOrderNumberValue.text = orderItem.orderNumber
                tvOrderDateValue.text = formatDate(orderItem.processedAt)
               tvOrderPriceValue.text = orderItem.totalPrice.amount +" "+orderItem.totalPrice.currencyCode
            }
        }
    }
    fun formatDate(dateString: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
        val outputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        val date = inputFormat.parse(dateString)
        return outputFormat.format(date)
    }
}
