package com.example.exclusive.ui.products.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.exclusive.R
import com.example.exclusive.data.remote.UiState
import com.example.exclusive.databinding.FragmentProductsBinding
import com.example.exclusive.model.ProductNode
import com.example.exclusive.ui.products.viewmodel.ProductsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlin.random.Random

@AndroidEntryPoint
class ProductsFragment : Fragment(), OnProductClickListener {

    private val viewModel: ProductsViewModel by viewModels()
    private var _binding: FragmentProductsBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: ProductsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.titleBar.tvTitle.text = getString(R.string.products)
        binding.rvProducts.layoutManager = GridLayoutManager(requireContext(), 2)
        adapter = ProductsAdapter(emptyList(), this)
        binding.rvProducts.adapter = adapter

        binding.titleBar.icBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        binding.subCategoryRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            val selectedCategory = when (checkedId) {
                R.id.radioBtnAccessories -> getString(R.string.accessories)
                R.id.radioBtnTShirts -> getString(R.string.t_shirts)
                R.id.radioBtnShoes -> getString(R.string.shoes)
                else -> getString(R.string.all)
            }
            adapter.filterProducts(selectedCategory)
        }

        binding.brandProductsRangedSeekbar.addOnChangeListener { slider, _, _ ->
            val values = slider.values
            val minValue = values[0].toInt()
            val maxValue = values[1].toInt()
            binding.minValue.text = "$minValue EGP"
            binding.maxValue.text = "$maxValue EGP"
            adapter.filterByPrice(minValue.toDouble(), maxValue.toDouble())
            adapter.notifyDataSetChanged()
        }

        val bundle = arguments
        val brandName = bundle?.getString("brand")
        brandName?.let { viewModel.fetchProducts(it) }

        lifecycleScope.launchWhenStarted {
            viewModel.uiState.collect { uiState ->
                when (uiState) {
                    is UiState.Success -> {
                        binding.progressBar.visibility = View.GONE
                        val products = uiState.data
                        if (products.isEmpty()) {
                            showZeroItems()
                        } else {
                            hideZeroItems()
                            products.forEach {
                                val min = 1.0f
                                val max = 5.0f
                                val randomFloat = getRandomFloat(min, max)
                                val formattedRandomFloat = formatFloat(randomFloat)
                                it.rating = formattedRandomFloat.toFloat()
                            }
                            adapter.updateProducts(products)
                        }
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

    private fun showZeroItems() {
        binding.zeroItems.visibility = View.VISIBLE
        binding.rvProducts.visibility = View.GONE
    }

    private fun hideZeroItems() {
        binding.zeroItems.visibility = View.GONE
        binding.rvProducts.visibility = View.VISIBLE
    }

    override fun onProductClick(product: ProductNode) {
        NavHostFragment.findNavController(this@ProductsFragment)
            .navigate(ProductsFragmentDirections.actionProductsFragmentToProductInfoFragment(product))
    }

    override fun onFavClick(product: ProductNode) {
        // Handle favorite click
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun getRandomFloat(min: Float, max: Float): Float {
        return Random.nextFloat() * (max - min) + min
    }

    fun formatFloat(value: Float): String {
        return String.format("%.1f", value)
    }
}
