package com.example.exclusive.ui.home.view

import HomeBrandsAdapter
import android.app.AlertDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.exclusive.R
import com.example.exclusive.data.model.DiscountCode
import com.example.exclusive.data.model.PriceRuleSummary
import com.example.exclusive.data.remote.UiState
import com.example.exclusive.databinding.FragmentHomeBinding
import com.example.exclusive.model.Brand
import com.example.exclusive.ui.home.viewmodel.HomeViewModel
import com.example.exclusive.utilities.Constants
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

private const val TAG = "HomeFragment"
@AndroidEntryPoint
class HomeFragment : Fragment(), OnItemClickListener, OnImageClickListener {
    private var couponDetailsJob: Job? = null
    private val viewModel: HomeViewModel by viewModels()
    private lateinit var adapter: HomeBrandsAdapter
    private lateinit var couponsAdapter: ImageSliderAdapter
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val handler = Handler(Looper.getMainLooper())
    private var currentPage = 0
    private var isDialogShowing = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val swipeRefreshLayout = binding.swipeRefreshLayout
        swipeRefreshLayout.setOnRefreshListener {
            swipeRefreshLayout.isRefreshing = false
            findNavController().popBackStack(R.id.homeFragment, false)
            findNavController().navigate(R.id.homeFragment)
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                requireActivity().finish()
            }
        })

        setViewsVisibility(View.GONE)
        binding.progressBar.visibility = View.VISIBLE

        autoScrollViewPager()

        val recyclerView = binding.rvCart
        recyclerView.layoutManager =
            GridLayoutManager(requireContext(), 2, GridLayoutManager.HORIZONTAL, false)
        adapter = HomeBrandsAdapter(emptyList(), this)
        recyclerView.adapter = adapter

        couponsAdapter = ImageSliderAdapter(listener = this)
        binding.viewPagerAdsSlider.adapter = couponsAdapter
        binding.viewPagerAdsSlider.setPageTransformer(ZoomOutPageTransformer())

        TabLayoutMediator(binding.tabLayout, binding.viewPagerAdsSlider) { tab, _ ->
            tab.setCustomView(R.layout.custom_tab)
        }.attach()

        lifecycleScope.launch {
            viewModel.uiState.collect { uiState ->
                when (uiState) {
                    is UiState.Success -> {
                        adapter.updateBrands(uiState.data.subList(1, uiState.data.size))
                        setViewsVisibility(View.VISIBLE)
                        binding.progressBar.visibility = View.GONE
                    }
                    is UiState.Error -> {
                        setViewsVisibility(View.GONE)
                        binding.progressBar.visibility = View.GONE
                    }
                    UiState.Loading -> {
                        setViewsVisibility(View.GONE)
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    UiState.Idle -> {
                        setViewsVisibility(View.GONE)
                        binding.progressBar.visibility = View.GONE
                    }
                }
            }
        }
        setupCoupons()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupCoupons() {
        lifecycleScope.launch {
            viewModel.discountState.collect { uiState ->
                when (uiState) {
                    is UiState.Success -> {
                        val validPriceRules = uiState.data.filter { it.isValid() }
                        Log.i(TAG, "setupCoupons: ${validPriceRules}")
                        couponsAdapter.submitList(validPriceRules)
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

    private fun setViewsVisibility(visibility: Int) {
        binding.viewPagerAdsSlider.visibility = visibility
        binding.rvCart.visibility = visibility
    }

    private fun autoScrollViewPager() {
        val runnable = object : Runnable {
            override fun run() {
                if (binding.viewPagerAdsSlider.adapter?.itemCount ?: 0 > 0) {
                    currentPage = (currentPage + 1) % binding.viewPagerAdsSlider.adapter?.itemCount!!
                    binding.viewPagerAdsSlider.setCurrentItem(currentPage, true)
                    handler.postDelayed(this, 3000)
                }
            }
        }
        handler.postDelayed(runnable, 3000)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        handler.removeCallbacksAndMessages(null)
    }

    override fun onItemClick(brand: Brand) {
        val bundle = Bundle().apply {
            putString("brand", brand.name)
        }
        findNavController().navigate(R.id.action_homeFragment_to_productFragment, bundle)
    }

    override fun onImageClick(priceRuleSummary: PriceRuleSummary) {
        if (!isDialogShowing) {
            isDialogShowing = true
            couponDetailsJob?.cancel()
            couponDetailsJob = lifecycleScope.launch {
                viewModel.fetchCouponDetails(id = priceRuleSummary.id)
                viewModel.couponDetailsState.collect { uiState ->
                    when (uiState) {
                        is UiState.Success -> {
                            val couponDetail = uiState.data
                            Constants.showCouponDetailDialog(requireActivity(), couponDetail, priceRuleSummary) {
                                isDialogShowing = false
                            }
                        }
                        is UiState.Error -> {
                            isDialogShowing = false
                        }
                        UiState.Idle -> {
                        }
                        UiState.Loading -> {
                        }
                    }
                }
            }
        }
    }
}
