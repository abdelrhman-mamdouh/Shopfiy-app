package com.example.exclusive.data.network

import com.example.exclusive.data.model.CouponsDetails
import com.example.exclusive.data.model.DiscountCode
import com.example.exclusive.data.model.PriceRuleSummary
import com.example.exclusive.data.model.PriceRulesResponse
import com.example.exclusive.data.network.DiscountApi
import com.example.exclusive.data.remote.DiscountDataSourceImpl
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import retrofit2.Response
class FakeDiscountDataSourceImplTest {
    @Test
    fun testGetPriceRules() {
        val fakeApi = object : DiscountApi {
            override suspend fun getPriceRules(accessToken: String): Response<PriceRulesResponse> {
                val mockResponse = PriceRulesResponse(
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
                return Response.success(mockResponse)
            }

            override suspend fun getCouponById(accessToken: String, id: Long): Response<CouponsDetails> {
                val mockResponse = CouponsDetails(
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
                return Response.success(mockResponse)
            }
        }

        val fakeDataSource = DiscountDataSourceImpl(fakeApi)
        val response = runBlocking { fakeDataSource.getPriceRules() }
        val priceRulesResponse = response.priceRules
        assertEquals(1, priceRulesResponse?.size)
        assertEquals("Rule1", priceRulesResponse?.get(0)?.title)
    }
}
