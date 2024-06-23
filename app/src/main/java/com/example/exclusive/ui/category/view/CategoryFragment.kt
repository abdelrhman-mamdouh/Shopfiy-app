package com.example.exclusive.ui.category.view

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
import com.example.exclusive.databinding.FragmentCategoryBinding
import com.example.exclusive.model.Brand
import com.example.exclusive.ui.category.viewmodel.CategoryViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class CategoryFragment : Fragment(), OnCategoryClickListener {

    private val viewModel: CategoryViewModel by viewModels()
    private var _binding: FragmentCategoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: CategoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val swipeRefreshLayout = binding.swipeRefreshLayout
        swipeRefreshLayout.setOnRefreshListener {
            swipeRefreshLayout.isRefreshing = false
            findNavController().popBackStack(R.id.categoryFragment, false)
            findNavController().navigate(R.id.categoryFragment)

        }
        val recyclerView = binding.recyclerview
        recyclerView.layoutManager =
            GridLayoutManager(requireContext(), 2, GridLayoutManager.HORIZONTAL, false)
        adapter = CategoryAdapter(emptyList(), this)
        recyclerView.adapter = adapter

        lifecycleScope.launch {
            viewModel.uiState.collect { uiState ->
                when (uiState) {
                    is UiState.Success -> {
                        binding.progressBar.visibility = View.GONE
                        adapter.updateCategory(uiState.data)
                    }

                    is UiState.Error -> {

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

    override fun onCategoryClick(brand: Brand) {
        val action = CategoryFragmentDirections.actionCategoryFragmentToProductsFragment(brand.name)
        findNavController().navigate(action)
    }
}
