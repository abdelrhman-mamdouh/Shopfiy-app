package com.example.exclusive.ui.productinfo

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.exclusive.data.local.LocalDataSource
import com.example.exclusive.data.remote.ShopifyRemoteDataSource
import com.example.exclusive.model.ProductNode
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.scopes.ViewScoped
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductInfoViewModel @Inject constructor(private val remoteDataSource: ShopifyRemoteDataSource,private val localDataSource: LocalDataSource):ViewModel() {
    private val _isWatchList = MutableStateFlow<Boolean>(false)
    val isWatchList: StateFlow<Boolean> = _isWatchList
    var accessToken : String? =null

    fun addProductToRealtimeDatabase(product: ProductNode){
        viewModelScope.launch {
            accessToken=localDataSource.token.first()
            remoteDataSource.addProductToRealtimeDatabase(product,accessToken.toString())
            _isWatchList.value = true
        }

    }
     fun isInWatchList(product: ProductNode){
        val result =viewModelScope.launch {
            accessToken=localDataSource.token.first()
            val watchlist = remoteDataSource.fetchWatchlistProducts(accessToken.toString())
            Log.d("watchlist", watchlist.toString())
            _isWatchList.value = watchlist.contains(product)
        }
    }
    fun removeProductFromWatchList(id: String){
       viewModelScope.launch {
           accessToken=localDataSource.token.first()
           remoteDataSource.deleteProduct(id,accessToken.toString())
           _isWatchList.value = false
       }
    }
}
