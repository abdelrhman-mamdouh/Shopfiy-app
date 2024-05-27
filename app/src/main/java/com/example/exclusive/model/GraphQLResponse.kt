package com.example.exclusive.model

import com.example.exclusive.type.CurrencyCode

data class Brand(val id: String, val name: String, val imageUrl: String)
data class MyProduct(
    val id: String,
    val title: String,
    val vendor: String,
    val productType: String,
    val imageUrl: String?,
    val price: Double,
    val currencyCode: String?
)
