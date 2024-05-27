package com.example.exclusive.data.remote

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.ApolloResponse
import com.apollographql.apollo3.exception.ApolloException
import com.example.exclusive.BrandsQuery
import com.example.exclusive.CategoriesQuery
import com.example.exclusive.ProductsQuery
import com.example.exclusive.model.Brand
import com.example.exclusive.model.MyProduct
import com.example.exclusive.utilities.Constants.API_KEY
import com.example.exclusive.utilities.Constants.BASE_URL

object ApolloService {

    private val apolloClient = ApolloClient.Builder()
        .serverUrl(BASE_URL)
        .addHttpHeader("X-Shopify-Storefront-Access-Token", API_KEY)
        .build()

    suspend fun getBrands(): List<Brand> {
        val brands = mutableListOf<Brand>()

        try {
            val response: ApolloResponse<BrandsQuery.Data> =
                apolloClient.query(BrandsQuery()).execute()

            response.data?.collections?.edges?.forEach { brand ->
                val id = brand.node.id
                val name = brand.node.title?: ""
                val imageUrl = brand.node.image?.originalSrc?: ""
                brands.add(Brand(id = id, name = name, imageUrl = imageUrl.toString()))
            }
        } catch (e: ApolloException) {
            println("ApolloException: ${e.message}")
        }

        return brands
    }


    suspend fun getProducts(vendor: String): List<MyProduct> {
        val products = mutableListOf<MyProduct>()

        try {
            val response: ApolloResponse<ProductsQuery.Data> =
                apolloClient.query(ProductsQuery(vendor = vendor)).execute()

            response.data?.products?.edges?.forEach { edge ->
                val node = edge.node
                val productPrice = node.priceRange?.minVariantPrice?.amount
                val priceString = productPrice.toString() ?: "0.0"
                val price = priceString.toDoubleOrNull() ?: 0.0
                products.add(MyProduct(
                    id = node.id,
                    title = node.title,
                    vendor = node.vendor,
                    productType = node.productType,
                    imageUrl = node.images.edges.firstOrNull()?.node?.src.toString(),
                    price = price,
                    currencyCode = node.priceRange.minVariantPrice.currencyCode.toString()
                ))
            }
        } catch (e: ApolloException) {
            println("ApolloException: ${e.message}")
        }

        return products
    }

    suspend fun getCategories(): List<Brand> {
        val brands = mutableListOf<Brand>()

        try {
            val response: ApolloResponse<CategoriesQuery.Data> =
                apolloClient.query(CategoriesQuery()).execute()

            response.data?.collections?.edges?.forEach { brand ->
                val id = brand.node.id
                val name = brand.node.title?: ""
                val imageUrl = brand.node.image?.originalSrc?: ""
                brands.add(Brand(id = id, name = name, imageUrl = imageUrl.toString()))
            }
        } catch (e: ApolloException) {
            println("ApolloException: ${e.message}")
        }

        return brands
    }

}
