package com.example.exclusive

import android.app.Application
import android.content.IntentFilter
import android.net.ConnectivityManager
import com.example.exclusive.reciever.ConnectivityReceiver
import com.google.firebase.FirebaseApp
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ShopApplication: Application(){
    private lateinit var connectivityReceiver: ConnectivityReceiver

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        // Initialize the receiver
        connectivityReceiver = ConnectivityReceiver()

        // Register the receiver for connectivity changes
        val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        registerReceiver(connectivityReceiver, filter)
    }
}

