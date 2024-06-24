package com.example.exclusive.data.model

import com.example.exclusive.model.BillingAddress

data class OrderRequest(
    val order: Order
)

data class Order(
    val email: String,
    val send_receipt: Boolean,
    val line_items: List<LineItem>,
    val billing_address: BillingAddress
)

data class LineItem(
    val variant_id: String,
    val quantity: Int
)
