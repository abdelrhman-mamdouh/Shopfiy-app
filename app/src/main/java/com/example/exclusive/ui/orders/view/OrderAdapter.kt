package com.example.exclusive.ui.orders.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.exclusive.databinding.RowOrderBinding
import com.example.exclusive.model.OrderItem


class OrderAdapter(
    private val orderList: List<OrderItem>, private val listener: OnOrderClickListener
) : RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val binding = RowOrderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OrderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        holder.bind(orderList[position])
    }

    override fun getItemCount() = orderList.size

    inner class OrderViewHolder(private val binding: RowOrderBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener.onOrderClick(orderList[position])
                }
            }
        }

        fun bind(orderItem: OrderItem) {
            binding.apply {
                tvOrderNumberValue.text = orderItem.orderNumber
                tvDateValue.text = orderItem.date
                tvTrackingNumber.text = orderItem.trackingNumber
                tvQuantity.text = orderItem.quantity.toString()
                tvTotalAmount.text = orderItem.totalAmount
                tvStatus.text = orderItem.status
            }
        }
    }
}
