package com.example.exclusive.data.model

import android.os.Build
import android.os.Parcelable
import androidx.annotation.RequiresApi
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.time.LocalDate
import java.time.OffsetDateTime


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
): Parcelable {
    @RequiresApi(Build.VERSION_CODES.O)
    fun isValid(): Boolean {
        val currentDate = OffsetDateTime.now().toLocalDate()
        val startDate = OffsetDateTime.parse(startsAt).toLocalDate()
        val endDate = OffsetDateTime.parse(endsAt).toLocalDate()
        return !currentDate.isBefore(startDate) && !currentDate.isAfter(endDate)
    }
}

