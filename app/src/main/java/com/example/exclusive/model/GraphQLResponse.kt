package com.example.exclusive.model

import android.os.Parcelable
import com.apollographql.apollo3.api.Optional
import com.example.exclusive.GetCheckoutDetailsQuery
import com.example.exclusive.type.Customer
import com.example.exclusive.type.MailingAddressInput
import kotlinx.android.parcel.Parcelize
import java.math.BigDecimal

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
    val description: String,
    val productType: String,
    val images: Images,
    val variants: Variants,
    var rating: Float = 4f
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
    var priceV2: PriceV2,
    val quantityAvailable: Int,
) : Parcelable


@Parcelize
data class PriceV2(
    val amount: String,
    var currencyCode: String
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
data class CheckoutDetails(
    val webUrl:String,
    val id: String,
    val createdAt: String,
    val completedAt: String?,
    val currencyCode: String,
    val totalPrice: PriceV2?,
    val lineItems: List<LineItem>,
    val discountApplications: List<DiscountCodeApplication>
)
data class DiscountCodeApplication(
    val code: String,
    val value: DiscountValue?
)

sealed class DiscountValue {
    data class Money(val amount: String, val currencyCode: String) : DiscountValue()
    data class Percentage(val percentage: String) : DiscountValue()
}
data class CartProduct(
    val id: String,
    val quantity: Int,
    val productId: String,
    val productTitle: String,
    val productImageUrl: String,
    val variantId: String,
    val variantTitle: String,
    var variantPrice: String,
    var variantPriceCode: String = "EGP"
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
    val variant: Variant,
    val price: Int
)
data class Variant(
    val id: String,
    val title: String,
    val price: PriceV2
)


data class AddressInput(
    val id: String?= null,
    val firstName: String,
    val lastName: String =firstName ,
    val phone: String,
    val address1: String,
    val city: String,
    val country: String,
    val zip: String,
    val province : String =city,
) {
    fun toInput(): MailingAddressInput {
        return MailingAddressInput(
            firstName = Optional.present(firstName),
            phone = Optional.present(phone),
            address1 = Optional.present(address1),
            city = Optional.present(city),
            country = Optional.present(country),
            zip = Optional.present(zip),
            province = Optional.present(province),
            lastName = Optional.present(lastName)
        )
    }
}
@Parcelize
data class MyOrder(
    val id: String,
    val name: String,
    val email: String,
    val processedAt: String,
    val orderNumber: String,
    val statusUrl: String,
    val phone: String?,
    val totalPrice :TotalPrice,
    val billingAddress: BillingAddress?,
    val lineItems: List<LineItems>
):Parcelable
@Parcelize
data class LineItems(
    val title: String,
    val quantity: Int,
    val imageUrl: String?,
    val originalTotalPrice: Price
):Parcelable
@Parcelize
data class BillingAddress(
    val address1: String?,
    val city: String?,
    val firstName:String?,
    val phone:String?
):Parcelable
@Parcelize
data class Price(
    val amount: String,
    val currencyCode: String
):Parcelable
@Parcelize
data class TotalPrice(
    val amount: String,
    val currencyCode: String
):Parcelable




