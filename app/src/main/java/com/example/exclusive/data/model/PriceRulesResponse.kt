package com.example.exclusive.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


data class PriceRulesResponse(
    @SerializedName("price_rules") val priceRules: List<PriceRuleSummary>
)
@Parcelize
data class PriceRuleSummary(
    val id: Long,
    val title: String,
    @SerializedName("value_type") val valueType: String,
    val value: Double,
    @SerializedName("customer_selection") val customerSelection: String,
    @SerializedName("usage_limit") val usageLimit: Int?,
    @SerializedName("starts_at") val startsAt: String,
    @SerializedName("ends_at") val endsAt: String
):Parcelable

data class CombinedData(
    val priceRule: PriceRuleSummary,
    val discountCode: DiscountCode
)