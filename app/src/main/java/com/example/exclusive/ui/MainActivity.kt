package com.example.exclusive.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.exclusive.R
import com.example.exclusive.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var authNavController: NavController? = null
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val authNavHostFragment =
            supportFragmentManager.findFragmentById(R.id.auth_nav_host_fragment) as NavHostFragment?
        authNavController = authNavHostFragment?.navController
        authNavController?.let { binding.bottomNavigationView.setupWithNavController(it) }

        authNavController?.addOnDestinationChangedListener { _, destination, _ ->
            binding.appBarHome.tvTitle.text = destination.label
            when (destination.id) {

                R.id.homeFragment, R.id.categoryFragment, R.id.settingsFragment -> {
                    binding.appBarHome.container.visibility = View.VISIBLE
                    binding.bottomNavigationView.visibility = View.VISIBLE

                    binding.appBarHome.cardViewShoppingCart.setOnClickListener {
                        authNavController?.navigate(R.id.cartFragment)
                    }
                    binding.appBarHome.imgViewFavorite.setOnClickListener {
                        authNavController?.navigate(R.id.watchlistFragment)
                    }
                    binding.appBarHome.imgViewSearch.setOnClickListener {
                        authNavController?.navigate(R.id.searchFragment)
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
