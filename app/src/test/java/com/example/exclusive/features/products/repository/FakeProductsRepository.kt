package com.example.exclusive.features.products.repository

import com.example.exclusive.model.AddToCartResponse
import com.example.exclusive.model.ImageEdge
import com.example.exclusive.model.ImageNode
import com.example.exclusive.model.Images
import com.example.exclusive.model.PriceV2
import com.example.exclusive.model.ProductNode
import com.example.exclusive.model.VariantEdge
import com.example.exclusive.model.VariantNode
import com.example.exclusive.model.Variants
import com.example.exclusive.type.CartLineInput
import com.example.exclusive.ui.products.repository.ProductsRepository

class FakeProductsRepository : ProductsRepository {
    private val mockCurrency = Pair("USD", 1.0)
    private val mockProducts = listOf(
        ProductNode(
            id = "1",
            title = "Product1",
            vendor = "Vendor1",
            description = "Description of Product1",
            productType = "Type1",
            images = Images(
                edges = listOf(
                    ImageEdge(
                        node = ImageNode(
                            src = "https://example.com/image1.jpg"
                        )
                    ),
                    ImageEdge(
                        node = ImageNode(
                            src = "https://example.com/image2.jpg"
                        )
                    )
                )
            ),
            variants = Variants(
                edges = listOf(
                    VariantEdge(
                        node = VariantNode(
                            id = "1",
                            title = "Variant1",
                            sku = "SKU001",
                            priceV2 = PriceV2(
                                amount = "10.00",
                                currencyCode = "USD"
                            ),
                            quantityAvailable = 100
                        )
                    ),
                    VariantEdge(
                        node = VariantNode(
                            id = "2",
                            title = "Variant2",
                            sku = "SKU002",
                            priceV2 = PriceV2(
                                amount = "15.00",
                                currencyCode = "USD"
                            ),
                            quantityAvailable = 50
                        )
                    )
                )
            ),
            rating = 4.5f
        )
    )
    override suspend fun getCurrentCurrency(): Pair<String, Double> {
        return mockCurrency
    }

    override suspend fun getProducts(vendor: String): List<ProductNode> {
        return mockProducts
    }

    override suspend fun getCartId(): String {
        TODO("Not yet implemented")
    }

    override suspend fun addProductToCart(
        cartId: String,
        lines: List<CartLineInput>
    ): AddToCartResponse? {
        TODO("Not yet implemented")
    }
}