package com.example.exclusive

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.exclusive.databinding.ActivityAuthMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthMain : AppCompatActivity() {
    private lateinit var binding: ActivityAuthMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}