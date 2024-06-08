package com.example.exclusive.ui.home.view

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.exclusive.R
import com.example.exclusive.data.model.PriceRuleSummary

private const val TAG = "ImageSliderAdapter"
class ImageSliderAdapter(
    private var couponList: List<PriceRuleSummary> = emptyList(),
    private val listener: OnImageClickListener
) : RecyclerView.Adapter<ImageSliderAdapter.ImageViewHolder>() {

    private val defaultDrawable = R.drawable.ic_launcher_background

    private val discountImageMap = mapOf(
        -10.0 to R.drawable.ad10,
        -20.0 to R.drawable.ad20,
        -30.0 to R.drawable.ad30,
        -40.0 to R.drawable.ad40,
        -50.0 to R.drawable.ad50
    ).withDefault { defaultDrawable }

    inner class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onImageClick(couponList[position])
                Log.d(TAG, "onClick: ${couponList[position].id.toInt()}")
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_image_slider, parent, false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val coupon = couponList[position]
        val imageRes = discountImageMap[coupon.value]
        if (imageRes != null) {
            holder.imageView.setImageResource(imageRes)
        }
    }

    override fun getItemCount(): Int {
        return couponList.size
    }

    fun updateCoupons(priceRule: List<PriceRuleSummary>) {
        couponList = priceRule
        notifyDataSetChanged()
    }
}