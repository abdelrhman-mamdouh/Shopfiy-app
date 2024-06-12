package com.example.exclusive

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.MutableLiveData

var internet:MutableLiveData<Boolean> = MutableLiveData(false)
class NoInternetActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_no_internet)
        internet.observe(this){
            if (it) {
                finish()
            }
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        Toast.makeText(this, "No Internet please try again", Toast.LENGTH_SHORT).show()
    }
}