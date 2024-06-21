package com.example.exclusive.ui.home.view

import com.example.exclusive.data.model.PriceRuleSummary
import com.example.exclusive.data.model.Brand


interface OnItemClickListener {

    fun onItemClick(brand: Brand)
}

interface OnImageClickListener {

    fun onImageClick(id: PriceRuleSummary)
}