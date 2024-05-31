package com.example.exclusive.ui.products.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.exclusive.databinding.RowProductBinding
import com.example.exclusive.model.MyProduct
import com.squareup.picasso.Picasso


class ProductsAdapter(
    private var allProducts: List<MyProduct>,
    private val listener: OnProductClickListener
) : RecyclerView.Adapter<ProductsAdapter.BrandViewHolder>() {

    private var filteredProducts: List<MyProduct> = allProducts

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

        fun bind(myProduct: MyProduct) {
            binding.tvProductName.text = myProduct.title
            binding.tvProductType.text = myProduct.productType
            binding.tvProductPrice.text = myProduct.price.toString() +" "+ myProduct.currencyCode.toString()
            if (!myProduct.imageUrl.isNullOrEmpty()) {
                Picasso.get().load(myProduct.imageUrl).into(binding.ivProductImage)
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
    fun filterByPrice(minPrice: Int, maxPrice: Int) {
        filteredProducts = allProducts.filter {
            it.price.toInt() in minPrice..maxPrice
        }
        notifyDataSetChanged()
    }
    fun updateProducts(newProducts: List<MyProduct>) {
        allProducts = newProducts
        filterProducts("All")
    }
}
