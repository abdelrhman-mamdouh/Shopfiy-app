package com.example.exclusive.data.remote

import android.util.Log
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.ApolloResponse
import com.apollographql.apollo3.exception.ApolloException
import com.example.exclusive.GetAllOrdersQuery
import com.example.exclusive.model.BillingAddress
import com.example.exclusive.model.LineItems
import com.example.exclusive.model.MyOrder
import com.example.exclusive.model.Price
import com.example.exclusive.model.TotalPrice
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OrderService @Inject constructor(private val apolloClient: ApolloClient) {
    suspend fun getAllOrders(customerAccessToken: String): List<MyOrder> {
        val orders = mutableListOf<MyOrder>()

        try {
            val response: ApolloResponse<GetAllOrdersQuery.Data> =
                apolloClient.query(GetAllOrdersQuery(customerAccessToken)).execute()
            Log.i("getAllOrders", "getAllOrders: ${customerAccessToken}+ ${response}")
            response.data?.customer?.orders?.edges?.forEach { edge ->
                val node = edge.node
                val id = node.id
                val name = node.name
                val email = node.email
                val processedAt = node.processedAt
                val orderNumber = node.orderNumber
                val statusUrl = node.statusUrl
                val phone = node.phone
                val totalPrice = node.totalPrice?.let {
                    TotalPrice(it.amount.toString(),it.currencyCode.toString())
                }
                val billingAddress = node.billingAddress?.let {
                    BillingAddress(it.address1, it.city,it.firstName,it.phone)
                }
                val lineItems = node.lineItems.edges.map { itemEdge ->
                    val itemNode = itemEdge.node
                    LineItems(
                        title = itemNode.title,
                        quantity = itemNode.quantity,
                        imageUrl = itemNode.variant?.image?.url.toString(),
                        originalTotalPrice = Price(
                            amount = itemNode.originalTotalPrice.amount.toString(),
                            currencyCode = itemNode.originalTotalPrice.currencyCode.toString()
                        )
                    )
                }

                orders.add(
                    MyOrder(
                        id = id,
                        name = name,
                        email = email!!,
                        processedAt = processedAt.toString(),
                        orderNumber = orderNumber.toString(),
                        statusUrl = statusUrl.toString(),
                        phone = phone,
                        totalPrice = totalPrice!!,
                        billingAddress = billingAddress,
                        lineItems = lineItems
                    )
                )
            }
        } catch (e: ApolloException) {
            println("ApolloException: ${e.message}")
        }

        return orders
    }
}