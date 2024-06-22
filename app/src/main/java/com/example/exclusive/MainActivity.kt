package com.example.exclusive

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.exclusive.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.auth_nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        binding.bottomNavigationView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            binding.appBarHome.tvTitle.text = destination.label
            when (destination.id) {
                R.id.homeFragment, R.id.categoryFragment, R.id.settingsFragment -> {
                    binding.appBarHome.container.visibility = View.VISIBLE
                    binding.bottomNavigationView.visibility = View.VISIBLE

                    binding.appBarHome.cardViewShoppingCart.setOnClickListener {
                        navController.navigate(R.id.cartFragment)
                    }
                    binding.appBarHome.imgViewFavorite.setOnClickListener {
                        navController.navigate(R.id.watchlistFragment)
                    }
                    binding.appBarHome.imgViewSearch.setOnClickListener {
                        navController.navigate(R.id.searchFragment)
                    }
                }
                else -> {
                    binding.appBarHome.container.visibility = View.GONE
                    binding.bottomNavigationView.visibility = View.GONE
                }
            }
        }
    }
}
