package com.example.exclusive

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.exclusive.databinding.ActivityAuthMainBinding
import com.example.exclusive.ui.auth.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AuthMain : AppCompatActivity() {
    private lateinit var binding: ActivityAuthMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel: AuthViewModel by viewModels()

        binding = ActivityAuthMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}