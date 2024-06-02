package com.example.exclusive.ui.auth

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.exclusive.data.local.LocalDataSource
import com.example.exclusive.data.remote.ShopifyRemoteDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "AuthViewModel"

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val remoteDataSource: ShopifyRemoteDataSource,
    private val localDataSource: LocalDataSource
) : ViewModel() {
    private val _signUpState = MutableStateFlow<Boolean>(false)
    val signUpState: StateFlow<Boolean> = _signUpState
    private val _loginState = MutableStateFlow<Boolean>(false)
    val loginState: StateFlow<Boolean> = _loginState
    val _sendPasswordState = MutableStateFlow<Boolean>(false)
    val sendPasswordState: StateFlow<Boolean> = _sendPasswordState
    val _resetPasswordState = MutableStateFlow<Boolean>(false)
    val resetPasswordState : StateFlow<Boolean> = _resetPasswordState
    val _tokenState = MutableStateFlow<String?>("")
    val tokenState : StateFlow<String?> = _tokenState
    fun getToken(){
        viewModelScope.launch {
            localDataSource.token.collect{
                _tokenState.value = it
            }
        }
    }
    fun signUp(name: String, email: String, password: String) {
        viewModelScope.launch {
            val isSignUp = remoteDataSource.createCustomer(email, password, name, "")
            if(isSignUp)
                createAccessToken(email, password)
            _signUpState.value = isSignUp
        }
    }

    private fun createAccessToken(email: String, password: String) {
        viewModelScope.launch {
            val result = remoteDataSource.createCustomerAccessToken(email, password)
            if (result != null) {
                localDataSource.saveToken(result)
                localDataSource.token.collect { token ->
                    Log.d(TAG, "login: $token")
                    val createResponse = remoteDataSource.createCart(token = token!!)
                    Log.d(TAG, "login: ${createResponse?.cart?.id?: "Not Found"}")
                    createResponse?.cart?.let { localDataSource.saveUserCartId(cartId = it.id) }
                }
            }
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            val result = remoteDataSource.createCustomerAccessToken(email, password)
            if (result != null) {
                _loginState.value = true
                localDataSource.saveToken(result)
                localDataSource.token.collect { token ->
                    Log.d(TAG, "login: $token")
                }
            } else {
                _loginState.value = false
            }
        }
    }

    fun sendPassword(email: String) {
        viewModelScope.launch {
            val result = remoteDataSource.sendPasswordRecoveryEmail(email)
            _sendPasswordState.value = result
        }
    }

    fun resetPassword(resetUrl: String, newPassword: String) {
        viewModelScope.launch {
            val result = remoteDataSource.resetPasswordByUrl(resetUrl, newPassword)
            _resetPasswordState.value = result
        }
    }
}