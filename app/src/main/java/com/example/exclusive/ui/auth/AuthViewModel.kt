package com.example.exclusive.ui.auth

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.exclusive.data.local.LocalDataSource
import com.example.exclusive.data.remote.ShopifyRemoteDataSource
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.auth
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

    private val auth: FirebaseAuth by lazy {
        Firebase.auth
    }
    private val _signUpState = MutableStateFlow<Int>(0)
    val signUpState: StateFlow<Int> get() = _signUpState

    private val _loginState = MutableStateFlow<Int>(0)
    val loginState: StateFlow<Int> get() = _loginState

    private val _sendPasswordState = MutableStateFlow<Boolean>(false)
    val sendPasswordState: StateFlow<Boolean> get() = _sendPasswordState

    private val _resetPasswordState = MutableStateFlow<Boolean>(false)
    val resetPasswordState: StateFlow<Boolean> get() = _resetPasswordState

    private val _tokenState = MutableStateFlow<String?>("")
    val tokenState: StateFlow<String?> get() = _tokenState

    fun getToken() {
        viewModelScope.launch {
            localDataSource.token.collect {
                _tokenState.value = it
            }
        }
    }

    fun signUp(name: String, email: String, password: String) {
        _signUpState.value = 0

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                if(auth.currentUser?.isEmailVerified?:true){
                    _signUpState.value = -1
                    return@addOnCompleteListener}
                auth.currentUser?.sendEmailVerification()?.addOnSuccessListener {
                    Log.d(TAG, "Verification email sent.")
                }?.addOnFailureListener { exception ->
                    _signUpState.value = -1
                    Log.d(TAG, "Failed to send verification email: ${exception.message}")
                }
                viewModelScope.launch {
                    val isSignUp = remoteDataSource.createCustomer(email, password, name, "")
                    _signUpState.value = if(isSignUp) 1 else -1
                 //   createAccessToken(email, password)
                }
            } else {
                Log.d(TAG, "Sign-up failed: ${task.exception.toString()}")
                if (task.exception is FirebaseAuthException) {
                    val errorCode = (task.exception as FirebaseAuthException).errorCode
                    Log.d(TAG, "FirebaseAuthException error code: $errorCode")
                }
                _signUpState.value = -1
            }
        }
    }

    private fun createAccessToken(email: String, password: String) {
        viewModelScope.launch {
            val result = remoteDataSource.createCustomerAccessToken(email, password)
            if (result != null) {
 
                localDataSource.token.collect { token ->
                    Log.d(TAG, "signUp: $token")
                    val createResponse = remoteDataSource.createCart(token = token!!)
                    Log.d(TAG, "signUp: ${createResponse?.cart?.id ?: "Not Found"}")
                    createResponse?.cart?.let {
                        remoteDataSource.saveCardId(it.id,email)
                    }
                }
            }
        }
    }

    fun login(email: String, password: String) {
        _loginState.value = 0
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val verification = auth.currentUser?.isEmailVerified
                if (verification == true) {
                    viewModelScope.launch {
                        localDataSource.saveEmail(auth.currentUser?.email ?: "email")
                        Log.d("email", localDataSource.readEmail().toString())
                        val result = remoteDataSource.createCustomerAccessToken(email, password)
                        if (result != null) {
                            _loginState.value = 1
                            localDataSource.saveToken(result)
                            localDataSource.token.collect { token ->
                                if (token != null) {
                                    val createResponse = remoteDataSource.createCart(token = token)
                                    Log.d(TAG, "signUp: ${createResponse?.cart?.id ?: "Not Found"}")
                                    createResponse?.cart?.let {
                                        remoteDataSource.saveCardId(it.id, email)
                                    }
                                } else {
                                    Log.d(TAG, "Token is null")
                                    _loginState.value = -1
                                }
                            }
                        } else {
                            _loginState.value = -1
                        }
                    }
                } else {
                    _loginState.value = -1
                    Log.d(TAG, "you must verify your email")
                }
            } else {
                Log.d(TAG, "Login failed: ${task.exception.toString()}")
                if (task.exception is FirebaseAuthException) {
                    val errorCode = (task.exception as FirebaseAuthException).errorCode
                    Log.d(TAG, "FirebaseAuthException error code: $errorCode")
                }
                _loginState.value = -1
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
    fun updateIsGuest(isGuest: Boolean) {
        viewModelScope.launch {
            localDataSource.updateIsGuest(isGuest)
        }
    }
}
