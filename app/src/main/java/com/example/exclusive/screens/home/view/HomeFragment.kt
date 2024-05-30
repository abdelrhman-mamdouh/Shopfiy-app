package com.example.exclusive.screens.home.view

import HomeBrandsAdapter
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.exclusive.HolderActivity
import com.example.exclusive.R
import com.example.exclusive.data.remote.UiState
import com.example.exclusive.databinding.FragmentCategoryBinding
import com.example.exclusive.databinding.FragmentHomeBinding
import com.example.exclusive.model.Brand
import com.example.exclusive.screens.category.view.CategoryAdapter
import com.example.exclusive.screens.category.view.OnCategoryClickListener
import com.example.exclusive.screens.category.viewmodel.CategoryViewModel
import com.example.exclusive.screens.home.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
@AndroidEntryPoint
class HomeFragment : Fragment(), OnBrandClickListener {

    private val viewModel: HomeViewModel by viewModels()
    private lateinit var adapter: HomeBrandsAdapter
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView = binding.recyclerView
        recyclerView.layoutManager =
            GridLayoutManager(requireContext(), 2, GridLayoutManager.HORIZONTAL, false)
        adapter = HomeBrandsAdapter(emptyList(), this)
        recyclerView.adapter = adapter

        lifecycleScope.launch {
            viewModel.uiState.collect { uiState ->
                when (uiState) {
                    is UiState.Success -> adapter.updateBrands(uiState.data)
                    is UiState.Error -> {

                    }

                    UiState.Loading -> {

                    }

                    UiState.Idle -> {

                    }
                }
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    override fun onBrandClick(brand: Brand) {
        val intent = Intent(context, HolderActivity::class.java)
        intent.putExtra("brand_name", brand.name)
        startActivity(intent)
    }
}
