package com.example.exclusive.screens.auth

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

@HiltViewModel
class AuthViewModel @Inject constructor(private val remoteDataSource: ShopifyRemoteDataSource,private val localDataSource: LocalDataSource) : ViewModel() {
    private val _signUpState = MutableStateFlow<Boolean>(false)
    val signUpState : StateFlow<Boolean> = _signUpState
    private val _loginState = MutableStateFlow<Boolean>(false)
    val loginState : StateFlow<Boolean> = _loginState
    val _sendPasswordState = MutableStateFlow<Boolean>(false)
    val sendPasswordState : StateFlow<Boolean> = _sendPasswordState
    val _resetPasswordState = MutableStateFlow<Boolean>(false)
    val resetPasswordState : StateFlow<Boolean> = _resetPasswordState
    fun signUp(name: String, email: String, password: String) {

        viewModelScope.launch {
         var  result=  remoteDataSource.createCustomer(email, password, name, "")
            if(result)
                _signUpState.value = true
            else
                _signUpState.value = false
        }

    }
    fun login(email: String, password: String) {
        viewModelScope.launch {
            var  result=  remoteDataSource.createCustomerAccessToken(email,password)
            if(result!=null){
                _loginState.value = true
                localDataSource.saveToken(result)
               localDataSource.token.collect{
                   Log.d("toooooooooooooooooooooooooken",it?:"")
               }
            }
            else
                _loginState.value = false
        }
    }
    fun sendPassword(email: String) {
        viewModelScope.launch {
            var  result=  remoteDataSource.sendPasswordRecoveryEmail(email)
            if(result)
                _sendPasswordState.value = true
            else
                _sendPasswordState.value = false
        }
    }
    fun resetPassword(resetUrl: String, newPassword: String) {
        viewModelScope.launch {
            var  result=  remoteDataSource.resetPasswordByUrl(resetUrl, newPassword)
            if(result)
                _resetPasswordState.value = true
            else
                _resetPasswordState.value = false
        }
    }
}