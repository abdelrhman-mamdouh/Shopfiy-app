package com.example.exclusive.ui.watchlist

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
class WatchlistViewModel@Inject constructor(private val remoteDataSource: ShopifyRemoteDataSource, private val localDataSource: LocalDataSource):
    ViewModel() {
        val _watchlist = MutableStateFlow<List<ProductNode>>(emptyList())
        val watchlist: StateFlow<List<ProductNode>> = _watchlist
        var accessToken : String? =null
        fun fetchWatchlist(){
            viewModelScope.launch {
                accessToken=localDataSource.token.first()
                val result = remoteDataSource.fetchWatchlistProducts(accessToken.toString())
                Log.d("ViewModelWatchlist", result.toString())
                _watchlist.value = result
            }
        }
        fun removeProductFromWatchList(id: String){
            viewModelScope.launch {
                accessToken=localDataSource.token.first()
                remoteDataSource.deleteProduct(id,accessToken.toString())
                fetchWatchlist()
            }
        }
}