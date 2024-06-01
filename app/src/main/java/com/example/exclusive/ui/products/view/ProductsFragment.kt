package com.example.exclusive.ui.products.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.exclusive.R
import com.example.exclusive.data.remote.UiState
import com.example.exclusive.databinding.FragmentProductsBinding
import com.example.exclusive.model.ProductNode
import com.example.exclusive.ui.products.viewmodel.ProductsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductsFragment : Fragment(), OnProductClickListener {

    private val viewModel: ProductsViewModel by viewModels()
    private var _binding: FragmentProductsBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: ProductsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
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
            requireActivity().onBackPressed()
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
            adapter.filterByPrice(minValue, maxValue)
            adapter.notifyDataSetChanged()
        }
        adapter.notifyDataSetChanged()

        val intent = activity?.intent
        val brandName = intent?.getStringExtra("brand_name")
        brandName?.let { viewModel.fetchProducts(it) }

        lifecycleScope.launchWhenStarted {
            viewModel.uiState.collect { uiState ->
                when (uiState) {
                    is UiState.Success -> adapter.updateProducts(uiState.data)
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

    override fun onProductClick(product: ProductNode) {
        viewModel.addToCart("gid://shopify/ProductVariant/45323456577790")
//        val bundle = Bundle()
//        bundle.putParcelable("product", product)
//
//        findNavController().navigate(R.id.action_productsFragment_to_cartFragment, bundle)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
