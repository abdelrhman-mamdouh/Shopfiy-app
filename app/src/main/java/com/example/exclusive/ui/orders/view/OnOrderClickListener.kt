package com.example.exclusive.ui.orders.view

import com.example.exclusive.model.Brand
import com.example.exclusive.model.OrderItem


interface OnOrderClickListener {

    fun onOrderClick(order: OrderItem)
}
