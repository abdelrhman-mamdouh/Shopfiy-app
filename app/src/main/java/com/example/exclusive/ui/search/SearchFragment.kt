package com.example.exclusive.ui.search

import HomeBrandsAdapter
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.activity.OnBackPressedCallback
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.exclusive.HolderActivity
import com.example.exclusive.data.remote.UiState
import com.example.exclusive.databinding.FragmentSearchBinding
import com.example.exclusive.model.Brand
import com.example.exclusive.model.ProductNode
import com.example.exclusive.ui.home.view.OnItemClickListener
import com.example.exclusive.ui.home.viewmodel.HomeViewModel
import com.example.exclusive.ui.productinfo.ProductInfoFragmentDirections
import com.example.exclusive.ui.products.view.OnProductClickListener
import com.example.exclusive.ui.products.view.ProductsAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@AndroidEntryPoint
class SearchFragment : Fragment(), OnProductClickListener {


    private val viewModel: SearchViewModel by viewModels()
    private lateinit var adapter: ProductsAdapter
    private lateinit var binding: FragmentSearchBinding
    private var products: List<ProductNode> = emptyList()
    override fun onResume() {
        super.onResume()
        binding.txtInputEditTextSearch.setText("")
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.icBack.setOnClickListener {
            requireActivity().onBackPressed()
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                requireActivity().finish()
            }
        })
        val recyclerView = binding.rvBrands
        viewModel.getProducts()


        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        adapter = ProductsAdapter(emptyList(),this)
        recyclerView.adapter = adapter
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.products.collect {
                    products = it
                    adapter = ProductsAdapter(it, this@SearchFragment)
                    adapter.notifyDataSetChanged()
                    recyclerView.adapter = adapter
                    binding.progressBar.visibility = View.GONE
                    Log.d("SearchFragment", it.toString())
                }
            }
        }
        binding.txtInputEditTextSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Not needed
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Handle text changes
                val text = s.toString()
                if (text.isNotEmpty()) {
                    adapter = ProductsAdapter(products.filter {
                        it.title.contains(text, ignoreCase = true)
                    }, this@SearchFragment)
                    adapter.notifyDataSetChanged()
                    recyclerView.adapter = adapter
                }

            }

            override fun afterTextChanged(s: Editable?) {
                // Not needed
            }
        })




    }

    override fun onProductClick(product: ProductNode) {
    findNavController().navigate(SearchFragmentDirections.actionSearchFragmentToProductInfoFragment(product
    ))
    //  findNavController().navigate(ProductInfoFragmentDirections.action_searchFragment_to_productInfoFragment(product))
    }

    override fun onFavClick(product: ProductNode) {

    }


}
