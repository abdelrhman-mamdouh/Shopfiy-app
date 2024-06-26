package com.example.exclusive.ui.products.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.exclusive.databinding.RowProductBinding
import com.example.exclusive.model.ProductNode
import com.squareup.picasso.Picasso
import kotlin.random.Random


class ProductsAdapter(
    private var allProducts: List<ProductNode>,
    private val listener: OnProductClickListener
) : RecyclerView.Adapter<ProductsAdapter.BrandViewHolder>() {

    private var filteredProducts: List<ProductNode> = allProducts

    inner class BrandViewHolder(private val binding: RowProductBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {


            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener.onProductClick(filteredProducts[position])
                }
            }
        }

        fun bind(myProduct: ProductNode) {
            binding.tvRating.text = myProduct.rating.toString()
            binding.rbProduct.rating = myProduct.rating
            binding.tvProductName.text = myProduct.title
            binding.tvProductType.text = myProduct.productType
            binding.tvProductPrice.text =
                myProduct.variants.edges.firstOrNull()?.node?.priceV2?.amount + " " + myProduct.variants.edges.firstOrNull()?.node?.priceV2?.currencyCode
            if (!myProduct.images.edges.firstOrNull()?.node?.src.isNullOrEmpty()) {
                Picasso.get().load(myProduct.images.edges[0].node.src).into(binding.ivProductImage)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BrandViewHolder {
        val binding = RowProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BrandViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BrandViewHolder, position: Int) {

        holder.bind(filteredProducts[position])
    }

    override fun getItemCount(): Int = filteredProducts.size

    fun filterProducts(category: String) {
        filteredProducts = if (category == "All") {
            allProducts
        } else {
            allProducts.filter { it.productType == category.toUpperCase() }
        }
        notifyDataSetChanged()
    }

    fun filterByPrice(minPrice: Double, maxPrice: Double) {
        filteredProducts = allProducts.filter {
            val price = it.variants.edges.firstOrNull()?.node?.priceV2?.amount?.toDoubleOrNull()
            price != null && price in minPrice..maxPrice
        }
        notifyDataSetChanged()
    }


    fun updateProducts(newProducts: List<ProductNode>) {
        allProducts = newProducts
        filterProducts("All")
    }



}
