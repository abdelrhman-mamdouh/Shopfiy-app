package com.example.exclusive.ui.home.view

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.exclusive.R
import com.example.exclusive.data.model.PriceRuleSummary

class ImageSliderAdapter(
    private val listener: OnImageClickListener
) : ListAdapter<PriceRuleSummary, ImageSliderAdapter.ImageViewHolder>(DiffCallback()) {

    inner class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onImageClick(getItem(position))
                Log.d("TAG", "onClick: ${getItem(position).id.toInt()}")
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_image_slider, parent, false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val coupon = getItem(position)
        val imageRes = when (coupon.value) {
            -10.0 -> R.drawable.ad10
            -20.0 -> R.drawable.ad20
            -30.0 -> R.drawable.ad30
            -40.0 -> R.drawable.ad40
            -50.0 -> R.drawable.ad50
            else -> R.drawable.coupon
        }
        holder.imageView.setImageResource(imageRes)
    }

    class DiffCallback : DiffUtil.ItemCallback<PriceRuleSummary>() {
        override fun areItemsTheSame(oldItem: PriceRuleSummary, newItem: PriceRuleSummary): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: PriceRuleSummary, newItem: PriceRuleSummary): Boolean {
            return oldItem == newItem
        }
    }
}