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

        if (intent.getStringExtra(GO_TO).equals("CART")) {
            findNavController(R.id.activity_holder_nav_host_fragment).navigate(R.id.cartFragment)
        } else if(intent.getStringExtra(GO_TO).equals("ADDRESS")) {
            findNavController(R.id.activity_holder_nav_host_fragment).navigate(R.id.addressesFragment)
        } else if (intent.getStringExtra(GO_TO).equals("CURRENCY")) {
            findNavController(R.id.activity_holder_nav_host_fragment).navigate(R.id.currenciesFragment)
        }
    }

    companion object {
        const val GO_TO = "go_to"
    }
}