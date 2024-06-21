package com.example.exclusive.ui.home.view

import com.example.exclusive.R

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.exclusive.data.model.SliderItem

import com.makeramen.roundedimageview.RoundedImageView

class SliderAdapter(
    private var sliderItems: MutableList<SliderItem>
) : RecyclerView.Adapter<SliderAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.slide_item_container, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.imgView?.setImageResource(sliderItems[position].image)
    }

    override fun getItemCount(): Int {
        return sliderItems.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imgView: RoundedImageView? = null
            get() {
                if (field == null) {
                    field = itemView.findViewById(R.id.imageSlider)
                }
                return field
            }
            private set
    }
}