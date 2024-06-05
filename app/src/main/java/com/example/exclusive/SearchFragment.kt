package com.example.exclusive

import HomeBrandsAdapter
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.exclusive.data.remote.UiState
import com.example.exclusive.databinding.FragmentSearchBinding
import com.example.exclusive.model.Brand
import com.example.exclusive.ui.home.view.OnItemClickListener
import com.example.exclusive.ui.home.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@AndroidEntryPoint
class SearchFragment : Fragment(), OnItemClickListener {

    private val viewModel: HomeViewModel by viewModels()
    private lateinit var adapter: HomeBrandsAdapter
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private var searchJob: Job? = null
    private var brandsList: List<Brand> = listOf()
    private var isListFetched = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
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
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        adapter = HomeBrandsAdapter(emptyList(), this)
        recyclerView.adapter = adapter

        binding.txtInputEditTextSearch.setOnEditorActionListener { textView, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val searchQuery = textView.text.toString()
                filterBrands(searchQuery)
                true
            } else {
                false
            }
        }

        if (!isListFetched) {
            observeList()
        }
    }

    private fun observeList() {
        lifecycleScope.launch {
            viewModel.uiState.collect { uiState ->
                when (uiState) {
                    is UiState.Success -> {
                        brandsList = uiState.data
                        adapter.updateBrands(brandsList)
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
        isListFetched = true
    }

    private fun filterBrands(searchQuery: String) {
        searchJob?.cancel()
        searchJob = lifecycleScope.launch {
            delay(300)
            val filteredBrands = brandsList.filter { brand ->
                brand.name.contains(searchQuery, ignoreCase = true)
            }
            adapter.updateBrands(filteredBrands)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        searchJob?.cancel()
    }

    override fun onItemClick(brand: Brand) {
        val intent = Intent(context, HolderActivity::class.java)
        intent.putExtra("brand_name", brand.name)
        startActivity(intent)
    }
}
