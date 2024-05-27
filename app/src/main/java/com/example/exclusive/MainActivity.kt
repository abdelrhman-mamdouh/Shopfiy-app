package com.example.exclusive

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI.setupWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.exclusive.data.remote.ShopifyRemoteDataSourceImpl
import com.example.exclusive.databinding.ActivityMainBinding
import com.example.exclusive.screens.home.repo.HomeRepositoryImpl
import com.example.exclusive.screens.home.view.BrandsAdapter
import com.example.exclusive.screens.home.viewmodel.BrandsViewModel
import com.example.exclusive.screens.home.viewmodel.HomeViewModelFactory
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
    }
}