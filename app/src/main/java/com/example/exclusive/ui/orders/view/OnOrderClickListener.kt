package com.example.exclusive.ui.orders.view

import com.example.exclusive.model.MyOrder

@FunctionalInterface
interface OnOrderClickListener {

    fun onOrderClick(order: MyOrder)
}
