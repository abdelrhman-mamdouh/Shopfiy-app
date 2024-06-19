package com.example.exclusive.ui.productinfo.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.exclusive.databinding.ImageItemBinding

class ImageDeffUtil: DiffUtil.ItemCallback<String>() {
    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }
}
class ImageAdapter(var imageList: List<String>): androidx.recyclerview.widget.ListAdapter<String, ImageAdapter.ImageViewHolder>(
    ImageDeffUtil()
){
    class ImageViewHolder(val binding:ImageItemBinding): RecyclerView.ViewHolder(binding.root)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        return ImageViewHolder(ImageItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.binding.apply {
            Glide.with(ivProductImage).load(imageList[position]).into(ivProductImage)
        }
    }

}
