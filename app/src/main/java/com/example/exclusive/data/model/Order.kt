package com.example.exclusive.data.model

data class Order(
    val id: Long,
    val admin_graphql_api_id: String,
    val app_id: Long?,
    val browser_ip: String?,
    val buyer_accepts_marketing: Boolean,
    val cancel_reason: String?,
    val cancelled_at: String?,
    val cart_token: String?,
    val checkout_id: String?,
    val checkout_token: String?,
    val client_details: Any?,
    val closed_at: String?,
    val company: String?,
    val confirmation_number: String?,
    val confirmed: Boolean,
    val contact_email: String?,
    val created_at: String,
    val currency: String,
    val current_subtotal_price: String,
    val current_subtotal_price_set: PriceSet,
    val current_total_additional_fees_set: Any?, // You can replace Any with an appropriate data class if needed
    val current_total_discounts: String,
    val current_total_discounts_set: PriceSet,
    val current_total_duties_set: Any?, // You can replace Any with an appropriate data class if needed
    val current_total_price: String,
    val current_total_price_set: PriceSet,
    val current_total_tax: String,
    val current_total_tax_set: PriceSet,
    val customer_locale: String?,
    val device_id: String?,
    val discount_codes: List<Any>, // You can replace Any with an appropriate data class if needed
    val email: String?,
    val estimated_taxes: Boolean,
    val financial_status: String,
    val fulfillment_status: String?,
    val landing_site: String?,
    val landing_site_ref: String?,
    val location_id: String?,
    val merchant_of_record_app_id: Any?, // You can replace Any with an appropriate data class if needed
    val name: String,
    val note: String?,
    val note_attributes: List<Any>, // You can replace Any with an appropriate data class if needed
    val number: Int,
    val order_number: Int,
    val order_status_url: String?,
    val original_total_additional_fees_set: Any?, // You can replace Any with an appropriate data class if needed
    val original_total_duties_set: Any?, // You can replace Any with an appropriate data class if needed
    val payment_gateway_names: List<Any>, // You can replace Any with an appropriate data class if needed
    val phone: String?,
    val po_number: String?,
    val presentment_currency: String,
    val processed_at: String,
    val reference: String?,
    val referring_site: String?,
    val source_identifier: String?,
    val source_name: String,
    val source_url: String?,
    val subtotal_price: String,
    val subtotal_price_set: PriceSet,
    val tags: String,
    val tax_exempt: Boolean,
    val tax_lines: List<Any>, // You can replace Any with an appropriate data class if needed
    val taxes_included: Boolean,
    val test: Boolean,
    val token: String,
    val total_discounts: String,
    val total_discounts_set: PriceSet,
    val total_line_items_price: String,
    val total_line_items_price_set: PriceSet,
    val total_outstanding: String,
    val total_price: String,
    val total_price_set: PriceSet,
    val total_shipping_price_set: PriceSet,
    val total_tax: String,
    val total_tax_set: PriceSet,
    val total_tip_received: String,
    val total_weight: Int,
    val updated_at: String,
    val user_id: String?,
    val billing_address: Any?, // You can replace Any with an appropriate data class if needed
    val customer: Customer,
    val discount_applications: List<Any>, // You can replace Any with an appropriate data class if needed
    val fulfillments: List<Any>, // You can replace Any with an appropriate data class if needed
    val line_items: List<LineItem>,
    val payment_terms: String?,
    val refunds: List<Any>, // You can replace Any with an appropriate data class if needed
    val shipping_address: Any?, // You can replace Any with an appropriate data class if needed
    val shipping_lines: List<Any> // You can replace Any with an appropriate data class if needed
)

data class PriceSet(
    val shop_money: Money,
    val presentment_money: Money
)

data class Money(
    val amount: String,
    val currency_code: String
)

data class Customer(
    val id: Long,
    val email: String,
    val created_at: String,
    val updated_at: String,
    val first_name: String,
    val last_name: String?,
    val state: String,
    val note: String?,
    val verified_email: Boolean,
    val multipass_identifier: String?,
    val tax_exempt: Boolean,
    val phone: String?,
    val email_marketing_consent: EmailMarketingConsent?,
    val sms_marketing_consent: Any?,
    val tags: String,
    val currency: String,
    val tax_exemptions: List<Any>
)

data class EmailMarketingConsent(
    val state: String,
    val opt_in_level: String,
    val consent_updated_at: String?
)

