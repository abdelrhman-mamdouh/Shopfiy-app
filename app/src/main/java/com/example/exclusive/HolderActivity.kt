package com.example.exclusive

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.example.exclusive.databinding.ActivityHolderBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HolderActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHolderBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHolderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent.getBooleanExtra(OPEN_CART, false)) {
            findNavController(R.id.activity_holder_nav_host_fragment).navigate(R.id.cartFragment)
        }
    }

    companion object {
        const val OPEN_CART = "open_cart"
    }
}