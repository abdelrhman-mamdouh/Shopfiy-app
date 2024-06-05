package com.example.exclusive.ui.watchlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.exclusive.databinding.WatchlistItemBinding
import com.example.exclusive.model.ProductNode

class WatchlistDiffUtil: DiffUtil.ItemCallback<ProductNode>() {
    override fun areItemsTheSame(oldItem: ProductNode, newItem: ProductNode): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ProductNode, newItem: ProductNode): Boolean {
        return oldItem == newItem
    }

}
class WatchListAdapter(var onRemoveListner:(ProductNode)->Unit,var onItemClickListener:(ProductNode)->Unit,var addToCart:(ProductNode)->Unit): androidx.recyclerview.widget.ListAdapter<ProductNode, WatchListAdapter.ViewHolder>(
    WatchlistDiffUtil()
) {
    class ViewHolder(val binding: WatchlistItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            WatchlistItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.binding.apply {
                val product = getItem(position)
                productBrand.text = product.vendor
                productName.text = product.title
                productPrice.text = "${product.variants.edges[0].node.priceV2.amount} ${product.variants.edges[0].node.priceV2.currencyCode}"
                //productRating.rating = product.rating.toFloat()
                textGroup.setOnClickListener {
                    onItemClickListener(product)
                }
                productImage.setOnClickListener{
                    onItemClickListener(product)
                }
                Glide.with(productImage)
                    .load(product.images.edges[0].node.src)
                    .into(productImage)
                deleteButton.setOnClickListener {
                    onRemoveListner(product)

                }
                cartButton.setOnClickListener {
                    addToCart(product)
                }

            }
        }
    }
