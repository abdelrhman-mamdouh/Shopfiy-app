package com.example.exclusive.ui.orders.view

import com.example.exclusive.data.model.MyOrder

@FunctionalInterface
interface OnOrderClickListener {

    fun onOrderClick(order: MyOrder)
}
