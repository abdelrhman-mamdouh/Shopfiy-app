package com.example.exclusive

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.exclusive.databinding.ActivityHolderBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HolderActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHolderBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHolderBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}