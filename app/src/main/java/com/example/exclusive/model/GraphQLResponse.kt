package com.example.exclusive.model

import android.os.Parcelable
import com.example.exclusive.type.CurrencyCode
import kotlinx.android.parcel.Parcelize

data class Brand(val id: String, val name: String, val imageUrl: String)
@Parcelize
data class MyProduct(
    val id: String,
    val title: String,
    val vendor: String,
    val productType: String,
    val imageUrl: String?,
    val price: Double,
    val currencyCode: String?
) : Parcelable

@Parcelize
data class OrderItem(
    val orderNumber: String,
    val date: String,
    val trackingNumber: String,
    val quantity: Int,
    val totalAmount: String,
    val status: String
) : Parcelable
data class ProductItem(
    val title: String,
    val color: String,
    val size: String,
    val units: String,
    val price: String,
    val imageUrl: String
)