package com.example.exclusive.features.home.repository


import com.example.exclusive.data.model.CouponsDetails
import com.example.exclusive.data.model.DiscountCode
import com.example.exclusive.data.model.PriceRuleSummary
import com.example.exclusive.data.model.PriceRulesResponse
import com.example.exclusive.model.Brand
import com.example.exclusive.ui.home.repository.HomeRepository

class FakeHomeRepository : HomeRepository {
    override suspend fun getBrands(): List<Brand> {
        return listOf(
            Brand(id = "1", name = "Brand1",""),
            Brand(id = "2", name = "Brand2","")
        )
    }

    override suspend fun getPriceRules(): PriceRulesResponse {
        return PriceRulesResponse(
            priceRules = listOf(
                PriceRuleSummary(
                    id = 1, title = "Rule1", valueType = "percentage", value = 10.0,
                    customerSelection = "all", usageLimit = 100, startsAt = "2023-01-01", endsAt = "2023-12-31"
                )
            )
        )
    }

    override suspend fun getCouponDetails(id: Long): CouponsDetails {
        return CouponsDetails(
            discount_codes = listOf(
                DiscountCode(
                    code = "DISCOUNT10", created_at = "2023-01-01", id = 1, price_rule_id = 1,
                    updated_at = "2023-01-02", usage_count = 0
                )
            )
        )
    }
}
