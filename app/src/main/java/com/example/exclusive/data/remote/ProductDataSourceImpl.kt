package com.example.exclusive.data.remote

import com.example.exclusive.data.remote.api.ProductService
import com.example.exclusive.data.remote.interfaces.ProductDataSource
import com.example.exclusive.data.model.Brand
import com.example.exclusive.data.model.ProductNode
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductDataSourceImpl @Inject constructor(
    private val productsService: ProductService
) : ProductDataSource {

    override suspend fun getBrands(): List<Brand> {
        return productsService.getBrands()
    }

    override suspend fun getCategories(): List<Brand> {
        return productsService.getCategories()
    }

    override suspend fun getProducts(vendor: String): List<ProductNode> {
        return productsService.getProducts(vendor)
    }

    override suspend fun getAllProducts(): List<ProductNode> {
        return productsService.getAllProducts()
    }
}