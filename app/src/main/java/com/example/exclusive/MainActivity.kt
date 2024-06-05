package com.example.exclusive

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.setupWithNavController
import com.example.exclusive.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        navController = Navigation.findNavController(this, R.id.activity_main_nav_host_fragment)
        binding.bottomNavigationView.setupWithNavController(navController)


        binding.appBarHome.cardViewShoppingCart.setOnClickListener {
            val intent = Intent(this, HolderActivity::class.java).apply {
                putExtra(HolderActivity.GO_TO, "CART")
            }
            startActivity(intent)
        }
        binding.appBarHome.imgViewFavorite.setOnClickListener {
            val intent = Intent(this, HolderActivity::class.java).apply {
                putExtra(HolderActivity.GO_TO, "FAV")
            }
            startActivity(intent)
        }
    }
}