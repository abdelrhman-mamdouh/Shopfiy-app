package com.example.exclusive.ui.productinfo

import android.content.res.Resources
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.exclusive.R
import com.example.exclusive.databinding.VarinetItemBinding
val colorsMap = mapOf("red" to R.color.red,
    "green" to R.color.green,
    "blue" to R.color.blue,
    "yellow" to R.color.yellow,
    "beige" to R.color.beige,
    "burgandy" to R.color.burgandy,
    "gray" to R.color.gray,
    "black" to R.color.black,
    "white" to R.color.white
)
class VarientDeffUtil: DiffUtil.ItemCallback<String>() {

    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }
}
class VarientAdapter(var onSelectListner:(String,Int)->Unit,var varientList: List<String>,var index:Int=-1): androidx.recyclerview.widget.ListAdapter<String, VarientAdapter.ViewHolder>(VarientDeffUtil()) {
    val colors = arrayOfNulls<Int>(varientList.size)

    class ViewHolder(val binding: VarinetItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            VarinetItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply {
           tvVarient.text = varientList[position]
            val varinets = varientList[position].split("/")

            val color = (if(varinets.size>1) varinets[1] else "green").replace(" ","")
            Log.d("color of varient", color)
            if (color == "white")
                tvVarient.setTextColor(ContextCompat.getColor(tvVarient.context, R.color.black))
            else
                tvVarient.setTextColor(ContextCompat.getColor(tvVarient.context, R.color.white))
            if(position==index){
                root.backgroundTintList = ContextCompat.getColorStateList(root.context, R.color.primary_color)
            }
            else
            root.backgroundTintList = ContextCompat.getColorStateList(root.context, colorsMap[color]?:R.color.green)
            root.setOnClickListener {

                onSelectListner(varientList[position],position)

               // it.backgroundTintList = ContextCompat.getColorStateList(it.context, colorsMap[color]?:R.color.green)
            }
        }
    }
}