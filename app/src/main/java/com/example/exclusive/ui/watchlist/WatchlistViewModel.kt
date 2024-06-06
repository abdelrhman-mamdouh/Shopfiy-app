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
        var email : String? =null
        fun fetchWatchlist(){
            viewModelScope.launch {
                email=localDataSource.readEmail()
                val result = remoteDataSource.fetchWatchlistProducts(email.toString())
                Log.d("ViewModelWatchlist", result.toString())
                _watchlist.value = result
            }
        }
        fun removeProductFromWatchList(id: String){
            viewModelScope.launch {
                email=localDataSource.readEmail()
                remoteDataSource.deleteProduct(id,email.toString())
                fetchWatchlist()
            }
        }
}