package com.example.exclusive.model

import android.os.Parcelable
import com.example.exclusive.exception.MyException
import com.example.exclusive.type.CurrencyCode
import kotlinx.android.parcel.Parcelize

data class Brand(val id: String, val name: String, val imageUrl: String)
@Parcelize
data class ProductQueryResponse(
    val products: Products
) : Parcelable

@Parcelize
data class Products(
    val edges: List<ProductEdge>
) : Parcelable

@Parcelize
data class ProductEdge(
    val node: ProductNode
) : Parcelable

@Parcelize
data class ProductNode(
    val id: String,
    val title: String,
    val vendor: String,
    val productType: String,
    val images: Images,
    val variants: Variants
) : Parcelable

@Parcelize
data class Images(
    val edges: List<ImageEdge>
) : Parcelable

@Parcelize
data class ImageEdge(
    val node: ImageNode
) : Parcelable

@Parcelize
data class ImageNode(
    val src: String
) : Parcelable

@Parcelize
data class Variants(
    val edges: List<VariantEdge>
) : Parcelable

@Parcelize
data class VariantEdge(
    val node: VariantNode
) : Parcelable

@Parcelize
data class VariantNode(
    val id: String,
    val title: String,
    val sku: String,
    val priceV2: PriceV2
) : Parcelable


@Parcelize
data class PriceV2(
    val amount: String,
    val currencyCode: String
) : Parcelable


@Parcelize
data class OrderItem(
    val orderNumber: String,
    val date: String,
    val trackingNumber: String,
    val quantity: Int,
    val totalAmount: String,
    val status: String
) : Parcelable
data class ProductItem(
    val title: String,
    val color: String,
    val size: String,
    val units: String,
    val price: String,
    val imageUrl: String
)

// Data classes for the createCard mutation
data class CreateCartResponse(
    val cart: Cart?,
    val userErrors: List<UserError>
)

data class Cart(
    val id: String
)

data class CartProductResponse(
    val products: List<CartProduct>,
    val userErrors: List<UserError>
)

data class UserError(
    val field: List<String>?,
    val message: String
)

data class CartProduct(
    val id: String,
    val quantity: Int,
    val productId: String,
    val productTitle: String,
    val productImageUrl: String,
    val variantId: String,
    val variantTitle: String,
    val variantPrice: String
)
data class MyCreateCartResponse(
    val cartCreate: MyCartCreate?
)

data class MyCartCreate(
    val cart: MyCart?,
    val userErrors: List<MyUserError>?
)

data class MyCart(
    val id: String
)

data class MyUserError(
    val field: List<String>?,
    val message: String
)

data class AddToCartResponse(
    val cart: Cart?,
    val userErrors: List<UserError>
)

data class CheckoutResponse(
    val checkout: Checkout?,
    val userErrors: List<UserError>
)

data class Checkout(
    val id: String,
    val webUrl: String,
    val lineItems: List<LineItem>
)

data class LineItem(
    val title: String,
    val quantity: Int,
    val variant: Variant
)

data class Variant(
    val id: String,
    val title: String,
    val price: String
)