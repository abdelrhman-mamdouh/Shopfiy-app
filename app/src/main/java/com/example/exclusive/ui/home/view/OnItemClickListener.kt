package com.example.exclusive.ui.home.view

import com.example.exclusive.model.Brand


interface OnItemClickListener {

    fun onItemClick(brand: Brand)
}

interface OnImageClickListener {

    fun onImageClick(item: Int)
}