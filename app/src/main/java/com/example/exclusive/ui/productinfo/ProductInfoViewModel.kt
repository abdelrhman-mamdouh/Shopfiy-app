package com.example.exclusive.ui.productinfo

import androidx.lifecycle.ViewModel
import com.example.exclusive.data.remote.ShopifyRemoteDataSource
import com.example.exclusive.model.ProductNode
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.scopes.ViewScoped
import javax.inject.Inject

@HiltViewModel
class ProductInfoViewModel @Inject constructor(private val remoteDataSource: ShopifyRemoteDataSource):ViewModel() {
    fun addProductToRealtimeDatabase(product: ProductNode){
        remoteDataSource.addProductToRealtimeDatabase(product)
    }
}
