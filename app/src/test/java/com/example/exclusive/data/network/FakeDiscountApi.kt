package com.example.exclusive.data.network

import com.example.exclusive.data.model.CouponsDetails
import com.example.exclusive.data.model.DiscountCode
import com.example.exclusive.data.model.PriceRuleSummary
import com.example.exclusive.data.model.PriceRulesResponse
import com.example.exclusive.data.network.DiscountApi
import com.example.exclusive.data.remote.DiscountDataSource
import retrofit2.Response

class FakeDiscountDataSource : DiscountDataSource {

    override suspend fun getPriceRules(): PriceRulesResponse {
        // Simulate a successful response with mock data
        return PriceRulesResponse(
            listOf(
                PriceRuleSummary(
                    id = 1,
                    title = "Rule1",
                    valueType = "percentage",
                    value = 10.0,
                    customerSelection = "all",
                    usageLimit = 100,
                    startsAt = "2023-01-01",
                    endsAt = "2023-12-31"
                )
            )
        )
    }

    override suspend fun getCouponDetails(id: Long): CouponsDetails {
        // Simulate a successful response with mock data
        return CouponsDetails(
            listOf(
                DiscountCode(
                    code = "DISCOUNT10",
                    created_at = "2023-01-01",
                    id = id,
                    price_rule_id = 1,
                    updated_at = "2023-01-02",
                    usage_count = 0
                )
            )
        )
    }
}
