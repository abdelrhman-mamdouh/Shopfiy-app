package com.example.exclusive.ui.productinfo

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.exclusive.R
import com.example.exclusive.databinding.VarinetItemBinding

// Color resource mapping for variant colors
val colorsMap = mapOf(
    "red" to R.color.red,
    "green" to R.color.green,
    "blue" to R.color.blue,
    "yellow" to R.color.yellow,
    "beige" to R.color.beige,
    "burgundy" to R.color.burgandy,
    "gray" to R.color.gray,
    "black" to R.color.black,
    "white" to R.color.white
)

// DiffUtil class for calculating differences in the list of variants
class VariantDiffUtil : DiffUtil.ItemCallback<String>() {
    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }
}

class VariantAdapter(
    private val onSelectListener: (String, Int) -> Unit,
    private val variantList: List<String>,
    private var selectedIndex: Int = 0 // Set the default selected index to 0
) : RecyclerView.Adapter<VariantAdapter.ViewHolder>() {

    class ViewHolder(val binding: VarinetItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = VarinetItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val variant = variantList[position]
        holder.binding.apply {
            tvVarient.text = variant

            val parts = variant.split("/")
            val colorName = if (parts.size > 1) parts[1].trim() else "green"


            val textColor = if (colorName.equals("white", ignoreCase = true)) {
                ContextCompat.getColor(tvVarient.context, R.color.black)
            } else {
                ContextCompat.getColor(tvVarient.context, R.color.white)
            }
            tvVarient.setTextColor(textColor)

            val backgroundColor = if (position == selectedIndex) {
                ContextCompat.getColorStateList(root.context, R.color.primary_color)
            } else {
                ContextCompat.getColorStateList(root.context, colorsMap[colorName] ?: R.color.green)
            }
            root.backgroundTintList = backgroundColor


            root.setOnClickListener {
                onSelectListener(variant, position)
                selectedIndex = position
                notifyDataSetChanged() // Refresh list to reflect selection change
            }
        }
    }

    override fun getItemCount(): Int {
        return variantList.size
    }
}
