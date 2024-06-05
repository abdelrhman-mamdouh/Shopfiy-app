package com.example.exclusive.ui.home.view

import HomeBrandsAdapter
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.exclusive.HolderActivity
import com.example.exclusive.R
import com.example.exclusive.data.remote.UiState
import com.example.exclusive.databinding.FragmentHomeBinding
import com.example.exclusive.model.Brand
import com.example.exclusive.ui.discountads.DiscountFragment

import com.example.exclusive.ui.home.viewmodel.HomeViewModel
import com.example.exclusive.utilities.SharedPreferencesManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment(),
    OnItemClickListener,OnImageClickListener {

    private val viewModel: HomeViewModel by viewModels()
    private lateinit var adapter: HomeBrandsAdapter
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val imageList = listOf(R.drawable.ad1, R.drawable.ad2, R.drawable.ad3)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val myAdapter = ImageSliderAdapter(requireContext(),imageList,this)
        binding.viewPagerAdsSlider.adapter = myAdapter



        val recyclerView = binding.rvCart
        recyclerView.layoutManager =
            GridLayoutManager(requireContext(), 2, GridLayoutManager.HORIZONTAL, false)
        adapter = HomeBrandsAdapter(emptyList(), this)
        recyclerView.adapter = adapter

        lifecycleScope.launch {
            viewModel.uiState.collect { uiState ->
                when (uiState) {
                    is UiState.Success -> {
                        adapter.updateBrands(uiState.data)

                        binding.progressBar.visibility = View.GONE
                    }

                    is UiState.Error -> {

                        binding.progressBar.visibility = View.GONE
                    }

                    UiState.Loading -> {

                        binding.progressBar.visibility = View.VISIBLE
                    }

                    UiState.Idle -> {

                        binding.progressBar.visibility = View.GONE
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClick(brand: Brand) {
        val intent = Intent(context, HolderActivity::class.java)
        intent.putExtra("brand_name", brand.name)
        startActivity(intent)
    }

    override fun onImageClick(item: Int) {
        lifecycleScope.launch {
            viewModel.discountState.collect { uiState ->
                when (uiState) {
                    is UiState.Success -> {
                        val priceRule = uiState.data[1]
                        SharedPreferencesManager.savePriceRule(requireContext(), priceRule)

                        val intent = Intent(requireContext(), HolderActivity::class.java).apply {
                            putExtra(HolderActivity.GO_TO, "ADDS")
                        }
                        startActivity(intent)
                    }

                    is UiState.Error -> {
                        Log.e("PriceRuleError", uiState.exception.toString())
                    }

                    UiState.Loading -> {
                        Log.d("PriceRule", "Loading...")
                    }

                    UiState.Idle -> {
                        Log.d("PriceRule", "Idle")
                    }
                }
            }
        }
    }

}
