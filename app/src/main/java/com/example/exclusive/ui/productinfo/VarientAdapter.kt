package com.example.exclusive.ui.productinfo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.exclusive.R
import com.example.exclusive.databinding.VarinetItemBinding

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
            if(position==index){
                root.backgroundTintList = ContextCompat.getColorStateList(root.context, R.color.black)
            }
            else
            root.backgroundTintList = ContextCompat.getColorStateList(root.context, R.color.primary_light_color)
            root.setOnClickListener {

                onSelectListner(varientList[position],position)
                it.backgroundTintList = ContextCompat.getColorStateList(it.context, R.color.black)
            }
        }
    }
}