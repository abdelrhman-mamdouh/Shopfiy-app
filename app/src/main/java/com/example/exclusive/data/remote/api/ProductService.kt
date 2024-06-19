package com.example.exclusive.data.remote.api

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.ApolloResponse
import com.apollographql.apollo3.exception.ApolloException
import com.example.exclusive.BrandsQuery
import com.example.exclusive.CategoriesQuery
import com.example.exclusive.GetAllProductsQuery
import com.example.exclusive.ProductsQuery
import com.example.exclusive.model.Brand
import com.example.exclusive.model.PriceV2
import com.example.exclusive.model.ProductNode
import com.example.exclusive.model.Variants
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG = "ProductService"
@Singleton
class ProductService @Inject constructor(private val apolloClient: ApolloClient) {

    suspend fun getBrands(): List<Brand> {
        val brands = mutableListOf<Brand>()

        try {
            val response: ApolloResponse<BrandsQuery.Data> =
                apolloClient.query(BrandsQuery()).execute()

            response.data?.collections?.edges?.forEach { brand ->
                val id = brand.node.id
                val name = brand.node.title ?: ""
                val imageUrl = brand.node.image?.originalSrc ?: ""
                brands.add(Brand(id = id, name = name, imageUrl = imageUrl.toString()))
            }
        } catch (e: ApolloException) {
            Timber.tag(TAG).d("getBrands: ${e.message}")
        }

        return brands
    }


    suspend fun getProducts(vendor: String): List<ProductNode> {
        val products = mutableListOf<ProductNode>()

        try {
            val response = apolloClient.query(ProductsQuery(vendor = vendor)).execute()
            val data = response.data

            data?.products?.edges?.forEach { edge ->
                val node = edge.node
                val productNode = ProductNode(
                    node.id,
                    node.title,
                    node.vendor,
                    node.description,
                    node.productType,
                    mapImages(node.images),
                    mapVariants(node.variants)
                )
                products.add(productNode)
            }
        } catch (e: ApolloException) {
            Timber.tag(TAG).d("getBrands: ${e.message}")
        }

        return products
    }

    suspend fun getAllProducts(): List<ProductNode> {
        val products = mutableListOf<ProductNode>()

        try {
            val response: ApolloResponse<GetAllProductsQuery.Data> =
                apolloClient.query(GetAllProductsQuery()).execute()
            val data = response.data
            data?.products?.edges?.forEach { edge ->
                val node = edge.node
                val productNode = ProductNode(
                    node.id,
                    node.title,
                    node.vendor,
                    node.description,
                    node.productType,
                    mapImages(node.images),
                    mapVariants(node.variants)
                )
                products.add(productNode)
            }
        } catch (e: ApolloException) {
            Timber.tag(TAG).d("getBrands: ${e.message}")
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
                val name = brand.node.title ?: ""
                val imageUrl = brand.node.image?.originalSrc ?: ""
                brands.add(Brand(id = id, name = name, imageUrl = imageUrl.toString()))
            }
        } catch (e: ApolloException) {
            Timber.tag(TAG).d("getBrands: ${e.message}")
        }

        return brands
    }

}

fun mapImages(productsQueryImages: ProductsQuery.Images): com.example.exclusive.model.Images {
    val imageEdges = productsQueryImages.edges.map { imageEdge ->
        com.example.exclusive.model.ImageEdge(
            com.example.exclusive.model.ImageNode(imageEdge.node.src.toString())
        )
    }
    return com.example.exclusive.model.Images(imageEdges)
}

fun mapImages(productsQueryImages: GetAllProductsQuery.Images): com.example.exclusive.model.Images {
    val imageEdges = productsQueryImages.edges.map { imageEdge ->
        com.example.exclusive.model.ImageEdge(
            com.example.exclusive.model.ImageNode(imageEdge.node.src.toString())
        )
    }
    return com.example.exclusive.model.Images(imageEdges)
}

fun mapVariants(productsQueryVariants: GetAllProductsQuery.Variants): Variants {
    val variantEdges = productsQueryVariants.edges.map { variantEdge ->
        com.example.exclusive.model.VariantEdge(
            com.example.exclusive.model.VariantNode(
                variantEdge.node.id,
                variantEdge.node.title,
                variantEdge.node.sku!!,
                PriceV2(
                    variantEdge.node.priceV2.amount.toString(),
                    variantEdge.node.priceV2.currencyCode.toString()
                ),
                variantEdge.node.quantityAvailable!!,
            )
        )
    }
    return Variants(variantEdges)
}

fun mapVariants(productsQueryVariants: ProductsQuery.Variants): Variants {
    val variantEdges = productsQueryVariants.edges.map { variantEdge ->
        com.example.exclusive.model.VariantEdge(
            com.example.exclusive.model.VariantNode(
                variantEdge.node.id,
                variantEdge.node.title,
                variantEdge.node.sku!!,
                PriceV2(
                    variantEdge.node.priceV2.amount.toString(),
                    variantEdge.node.priceV2.currencyCode.toString()
                ),
                variantEdge.node.quantityAvailable!!
            )
        )
    }
    return Variants(variantEdges)
}
