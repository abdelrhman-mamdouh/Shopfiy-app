package com.example.exclusive.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import com.example.exclusive.R

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