package com.example.exclusive.ui.category.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.exclusive.databinding.RowBrandBinding
import com.example.exclusive.model.Brand
import com.squareup.picasso.Picasso


class CategoryAdapter(
    private var categories: List<Brand>,
    private val listener: OnCategoryClickListener
) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    inner class CategoryViewHolder(private val binding: RowBrandBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener.onCategoryClick(categories[position])
                }
            }
        }
        fun bind(brand: Brand) {
            binding.tvBrandName.text = brand.name
            if (!brand.imageUrl.isNullOrEmpty()) {
                Picasso.get().load(brand.imageUrl).into(binding.ivBrandImage)
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding = RowBrandBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(categories[position])
    }

    override fun getItemCount(): Int = categories.size

    fun updateCategory(newBrands: List<Brand>) {
        categories = newBrands
        notifyDataSetChanged()
    }
}