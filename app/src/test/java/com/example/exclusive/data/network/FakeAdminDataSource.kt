package com.example.exclusive.data.network

import com.example.exclusive.data.model.CouponsDetails
import com.example.exclusive.data.model.DiscountCode
import com.example.exclusive.data.model.OrderRequest
import com.example.exclusive.data.model.PriceRuleSummary
import com.example.exclusive.data.model.PriceRulesResponse
import com.example.exclusive.data.remote.AdminRemoteDataSource
import com.example.exclusive.model.BillingAddress
import com.example.exclusive.model.LineItems
import com.example.exclusive.model.MyOrder
import com.example.exclusive.model.Price
import com.example.exclusive.model.TotalPrice

class FakeAdminDataSource : AdminRemoteDataSource {

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

    override suspend fun createOrder(orderRequest: OrderRequest): MyOrder {
        val billingAddress = BillingAddress(
            first_name = "abdelrhman",
            last_name = "Doe",
            address1 = "Sample Country",
            city = "cairo",
            province = "cairo",
            country = "Egypt",
            zip = "1123",
            phone = "01153716828"
        )

        val lineItems = listOf(
            LineItems(
                title = "Product A",
                quantity = 1,
                imageUrl = "da",
                originalTotalPrice = Price(
                    currencyCode = "EGY",
                    amount = "50"
                )
            )
        )

        return MyOrder(
            id = "order123",
            name = "abdelrhman",
            email = "abdelrhman@example.com",
            processedAt = "2023-06-24T12:00:00Z",
            orderNumber = "ORD-12345",
            statusUrl = "https://example.com/orders/order123",
            phone = "123-456-7890",
            totalPrice = TotalPrice(
                amount = "50.0",
                currencyCode = "USD"
            ),
            billingAddress = billingAddress,
            lineItems = lineItems
        )
    }

}

