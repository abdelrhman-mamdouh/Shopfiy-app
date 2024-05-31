package com.example.exclusive.ui.settings.currency

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.exclusive.databinding.ItemCurrencyBinding


class CurrenciesAdapter(private var currencies: List<Pair<String, String>>, private val clickListener: ClickListener) : RecyclerView.Adapter<CurrenciesAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemCurrencyBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        Log.d("CurrenciesAdapter", "onCreateViewHolder")
        val binding =
            ItemCurrencyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        Log.d("CurrenciesAdapter", "getItemCount: ${currencies.size}")
        return currencies.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.d("CurrenciesAdapter", "onBindViewHolder: $position")
        val (currencyCode, currencyName) = currencies[position]
        holder.binding.tvName.text = currencyName
        holder.binding.tvCode.text = currencyCode
        holder.binding.root.setOnClickListener {
            clickListener.onClick(currencyCode)
        }
    }

    fun updateCurrencies(currencies: List<Pair<String, String>>) {
        this.currencies = currencies
        notifyDataSetChanged()
        Log.d("CurrenciesAdapter", "updateCurrencies: ${currencies.size} items")
    }

    class ClickListener(private val clickListener: (String) -> Unit) {
        fun onClick(currencyCode: String) = clickListener(currencyCode)
    }
}