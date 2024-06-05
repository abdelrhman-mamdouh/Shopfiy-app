package com.example.exclusive.ui.productinfo

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.exclusive.data.local.LocalDataSource
import com.example.exclusive.data.remote.ShopifyRemoteDataSource
import com.example.exclusive.model.ProductNode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductInfoViewModel @Inject constructor(private val remoteDataSource: ShopifyRemoteDataSource,private val localDataSource: LocalDataSource):ViewModel() {
    private val _isWatchList = MutableStateFlow<Boolean>(false)
    val isWatchList: StateFlow<Boolean> = _isWatchList
    var email : String? =null

    fun addProductToRealtimeDatabase(product: ProductNode){
        viewModelScope.launch {
            email=localDataSource.readEmail()
            Log.d("readEmail", email.toString())
            remoteDataSource.addProductToRealtimeDatabase(product,email.toString())
            _isWatchList.value = true
        }

    }
     fun isInWatchList(product: ProductNode){
        val result =viewModelScope.launch {
            email=localDataSource.readEmail()
            val watchlist = remoteDataSource.fetchWatchlistProducts(email.toString())
            Log.d("watchlist", watchlist.toString())
            var bool = false
            watchlist.forEach {
                if(it.id==product.id)
                    bool = true
            }
            _isWatchList.value = bool
        }
    }
    fun removeProductFromWatchList(id: String){
       viewModelScope.launch {
           email=localDataSource.readEmail()
           remoteDataSource.deleteProduct(id,email.toString())
           _isWatchList.value = false
       }
    }
}
