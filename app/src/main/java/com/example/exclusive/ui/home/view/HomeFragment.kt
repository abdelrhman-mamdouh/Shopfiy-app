package com.example.exclusive.ui.home.view

import HomeBrandsAdapter
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.exclusive.HolderActivity
import com.example.exclusive.data.model.DiscountCode
import com.example.exclusive.data.remote.UiState
import com.example.exclusive.databinding.FragmentHomeBinding
import com.example.exclusive.model.Brand
import com.example.exclusive.ui.home.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

private const val TAG = "HomeFragment"
@AndroidEntryPoint
class HomeFragment : Fragment(), OnItemClickListener, OnImageClickListener {

    private val viewModel: HomeViewModel by viewModels()
    private lateinit var adapter: HomeBrandsAdapter
    private lateinit var couponsAdapter: ImageSliderAdapter
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val handler = Handler(Looper.getMainLooper())
    private var currentPage = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

        lifecycleScope.launch {
            viewModel.uiState.collect { uiState ->
                when (uiState) {
                    is UiState.Success -> {
                        adapter.updateBrands(uiState.data)
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

        lifecycleScope.launch {
            viewModel.couponDetailsState.collect { uiState ->
                when (uiState) {
                    is UiState.Success -> {
                        val couponDetail = uiState.data
                        Log.d(TAG, "onViewCreated: $couponDetail")
                        showCouponDetailDialog(couponDetail)
                    }
                    is UiState.Error -> {
                        Log.e("CouponDetailsError", uiState.exception.toString())
                    }
                    UiState.Loading -> {
                        Log.d("CouponDetails", "Loading...")
                    }
                    UiState.Idle -> {
                        Log.d("CouponDetails", "Idle")
                    }
                }
            }
        }
        setupCoupons()
    }

    private fun showCouponDetailDialog(couponDetail: DiscountCode) {
        AlertDialog.Builder(requireContext())
            .setTitle("Coupon Details")
            .setMessage("Code: ${couponDetail.code}\nValue: ${couponDetail.price_rule_id}")
            .setPositiveButton("OK", null)
            .show()
    }

    private fun setupCoupons() {
        lifecycleScope.launch {
            viewModel.discountState.collect { uiState ->
                when (uiState) {
                    is UiState.Success -> {
                        val priceRule = uiState.data
                        Log.d(TAG, "setupCoupons: $priceRule")
                        couponsAdapter.updateCoupons(priceRule)
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
        val intent = Intent(context, HolderActivity::class.java)
        intent.putExtra("brand_name", brand.name)
        startActivity(intent)
    }

    override fun onImageClick(id: Long) {
        Log.d("TAG", "onImageClick: $id")
        viewModel.fetchCouponDetails(id = id)
    }
}
