package com.example.exclusive

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import com.example.exclusive.screens.home.viewmodel.ProductViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: ProductViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }
}