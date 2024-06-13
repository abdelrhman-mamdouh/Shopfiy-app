package com.example.exclusive.ui.settings.currency

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.example.exclusive.databinding.ItemCurrencyBinding


class CurrenciesAdapter(
    private var currencies: List<Pair<String, String>>,
    private val clickListener: ClickListener
) : RecyclerView.Adapter<CurrenciesAdapter.ViewHolder>(), Filterable {

    private var filteredCurrencies: List<Pair<String, String>> = currencies

    class ViewHolder(val binding: ItemCurrencyBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCurrencyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return filteredCurrencies.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val (currencyCode, currencyName) = filteredCurrencies[position]
        holder.binding.tvName.text = currencyName
        holder.binding.tvCode.text = currencyCode
        holder.binding.root.setOnClickListener {
            clickListener.onClick(currencyCode)
        }
    }

    fun updateCurrencies(currencies: List<Pair<String, String>>) {
        this.currencies = currencies
        this.filteredCurrencies = currencies
        notifyDataSetChanged()
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charString = constraint?.toString() ?: ""
                filteredCurrencies = if (charString.isEmpty()) currencies else {
                    currencies.filter {
                        it.second.contains(charString, true) || it.first.contains(charString, true)
                    }
                }
                return FilterResults().apply { values = filteredCurrencies }
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredCurrencies = if (results?.values == null) listOf() else results.values as List<Pair<String, String>>
                notifyDataSetChanged()
            }
        }
    }

    class ClickListener(private val clickListener: (String) -> Unit) {
        fun onClick(currencyCode: String) = clickListener(currencyCode)
    }
}