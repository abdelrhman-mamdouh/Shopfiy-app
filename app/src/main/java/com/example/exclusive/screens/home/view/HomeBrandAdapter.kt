package com.example.exclusive.screens.home.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.exclusive.R
import com.example.exclusive.model.Brand
import com.squareup.picasso.Picasso


class BrandsAdapter(private var brands: List<Brand>) : RecyclerView.Adapter<BrandsAdapter.BrandViewHolder>() {

    class BrandViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.tv_brandName)
        val imageView: ImageView = itemView.findViewById(R.id.iv_brandImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BrandViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_brand, parent, false)
        return BrandViewHolder(view)
    }

    override fun onBindViewHolder(holder: BrandViewHolder, position: Int) {
        val brand = brands[position]
        holder.nameTextView.text = brand.name
        if (!brand.imageUrl.isNullOrEmpty()) {
            Picasso.get().load(brand.imageUrl).into(holder.imageView)
        }
    }

    override fun getItemCount(): Int = brands.size

    fun updateBrands(newBrands: List<Brand>) {
        brands = newBrands
        notifyDataSetChanged()
    }
}
