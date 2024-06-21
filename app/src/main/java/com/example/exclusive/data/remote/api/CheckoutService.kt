package com.example.exclusive.data.remote.api

import android.util.Log
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.ApolloResponse
import com.apollographql.apollo3.api.Optional
import com.apollographql.apollo3.exception.ApolloException
import com.example.exclusive.ApplyDiscountCodeMutation
import com.example.exclusive.ApplyShippingAddressMutation
import com.example.exclusive.CreateCheckoutMutation
import com.example.exclusive.GetCheckoutDetailsQuery
import com.example.exclusive.data.model.Checkout
import com.example.exclusive.data.model.CheckoutDetails
import com.example.exclusive.data.model.CheckoutResponse
import com.example.exclusive.data.model.DiscountCodeApplication
import com.example.exclusive.data.model.DiscountValue
import com.example.exclusive.data.model.LineItem
import com.example.exclusive.data.model.PriceV2
import com.example.exclusive.data.model.UserError
import com.example.exclusive.data.model.Variant
import com.example.exclusive.type.CheckoutLineItemInput
import com.example.exclusive.type.MailingAddressInput
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CheckoutService @Inject constructor(private val apolloClient: ApolloClient) {
    suspend fun createCheckout(
        lineItems: List<CheckoutLineItemInput>, email: String?
    ): CheckoutResponse? {
        val mutation = CreateCheckoutMutation(
            lineItems = lineItems, email = Optional.Present(email)
        )

        return try {
            val response = apolloClient.mutation(mutation).execute()
            val checkoutCreate = response.data?.checkoutCreate
            val checkout = checkoutCreate?.checkout?.let {
                Checkout(id = it.id,
                    webUrl = it.webUrl.toString(),
                    lineItems = it.lineItems.edges.map { edge ->
                        LineItem(
                            title = edge.node.title,
                            quantity = edge.node.quantity,
                            variant = Variant(
                                id = edge.node.variant!!.id,
                                title = edge.node.variant.title,
                                price = PriceV2(
                                    amount = edge.node.variant.price.amount.toString(),
                                    currencyCode = ""
                                )
                            ),
                            45
                        )
                    })
            }
            val userErrors =
                checkoutCreate?.userErrors?.map { UserError(it.field, it.message) } ?: emptyList()

            CheckoutResponse(checkout, userErrors)
        } catch (e: ApolloException) {
            Log.e("GraphQL", "ApolloException: ${e.message}", e)
            null
        }
    }

    suspend fun applyDiscountCode(checkoutId: String, discountCode: String): Boolean {
        val mutation = ApplyDiscountCodeMutation(
            checkoutId = checkoutId, discountCode = discountCode
        )
        return try {
            val response: ApolloResponse<ApplyDiscountCodeMutation.Data> =
                apolloClient.mutation(mutation).execute()

            val errors = response.data?.checkoutDiscountCodeApplyV2?.checkoutUserErrors
            if (errors != null && errors.isNotEmpty()) {
                for (error in errors) {
                    Log.e("GraphQL", "Error: ${error.message}")
                }
                false
            } else {
                val checkout = response.data?.checkoutDiscountCodeApplyV2?.checkout
                Log.d(
                    "GraphQL", "Discount code applied successfully to checkout ID: ${checkout?.id}"
                )
                true
            }
        } catch (e: ApolloException) {
            Log.e("GraphQL", "ApolloException: ${e.message}", e)
            false
        }
    }

    suspend fun getCheckoutDetails(checkoutId: String): CheckoutDetails? {
        val query = GetCheckoutDetailsQuery(checkoutId = checkoutId)

        return try {
            val response: ApolloResponse<GetCheckoutDetailsQuery.Data> =
                apolloClient.query(query).execute()
            val checkout = response.data?.node?.onCheckout
            if (checkout != null) {
                val discountApplications =
                    checkout.discountApplications?.edges?.mapNotNull { edge ->
                        edge.node?.onDiscountCodeApplication?.let { node ->
                            val value = node.value
                            when {
                                value?.onMoneyV2 != null -> {
                                    DiscountValue.Money(
                                        amount = value.onMoneyV2.amount.toString(),
                                        currencyCode = value.onMoneyV2.currencyCode.toString()
                                    )
                                }

                                value?.onPricingPercentageValue != null -> {
                                    DiscountValue.Percentage(value.onPricingPercentageValue.percentage.toString())
                                }

                                else -> null
                            }?.let { discountValue ->
                                DiscountCodeApplication(
                                    code = node.code ?: "", value = discountValue
                                )
                            }
                        }
                    } ?: emptyList()

                CheckoutDetails(webUrl = checkout.webUrl.toString(),
                    id = checkout.id,
                    createdAt = checkout.createdAt.toString(),
                    completedAt = checkout.completedAt?.toString(),
                    currencyCode = checkout.currencyCode.toString(),
                    totalPrice = checkout.totalPriceV2?.let {
                        PriceV2(
                            amount = it.amount.toString(), currencyCode = it.currencyCode.toString()
                        )
                    },
                    lineItems = checkout.lineItems?.edges?.mapNotNull { edge ->
                        edge.node?.variant?.let { variant ->
                            Variant(
                                id = variant.id,
                                title = variant.title,
                                price = variant.priceV2?.let {
                                    PriceV2(
                                        amount = it.amount.toString(),
                                        currencyCode = it.currencyCode.toString()
                                    )
                                }!!
                            )
                        }?.let { variant ->
                            LineItem(
                                title = edge.node?.title ?: "",
                                quantity = edge.node?.quantity ?: 0,
                                variant = variant,
                                45
                            )
                        }
                    } ?: emptyList(),
                    discountApplications = discountApplications)
            } else {
                null
            }
        } catch (e: ApolloException) {
            Log.e("Apollo", "Error retrieving checkout details: ${e.message}", e)
            null
        }
    }

    suspend fun applyShippingAddress(checkoutId: String, shippingAddress: MailingAddressInput): Boolean {
        val mutation = ApplyShippingAddressMutation(checkoutId = checkoutId, shippingAddress = shippingAddress)
        return try {
            val response: ApolloResponse<ApplyShippingAddressMutation.Data> =
                apolloClient.mutation(mutation).execute()

            val errors = response.data?.checkoutShippingAddressUpdateV2?.checkoutUserErrors
            if (!errors.isNullOrEmpty()) {
                for (error in errors) {
                    Log.e("GraphQL", "Error: ${error.message}")
                }
                false
            } else {
                val checkout = response.data?.checkoutShippingAddressUpdateV2?.checkout
                if (checkout != null) {
                    Log.d("GraphQL", "Shipping address applied successfully to checkout ID: ${checkout.id}")
                    Log.d("GraphQL", "Shipping address applied successfully to checkout webUrl: ${checkout.webUrl}")
                    true
                } else {
                    false
                }
            }
        } catch (e: ApolloException) {
            Log.e("GraphQL", "ApolloException: ${e.message}", e)
            false
        }
    }
}