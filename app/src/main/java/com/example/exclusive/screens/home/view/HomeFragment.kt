package com.example.exclusive.screens.home.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.exclusive.R
import com.example.exclusive.data.remote.ShopifyRemoteDataSourceImpl
import com.example.exclusive.screens.home.repo.HomeRepositoryImpl
import com.example.exclusive.screens.home.viewmodel.BrandsViewModel
import com.example.exclusive.screens.home.viewmodel.HomeViewModelFactory

class HomeFragment : Fragment() {

    private lateinit var viewModel: BrandsViewModel
    private lateinit var adapter: BrandsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_home, container, false)
        val recyclerView: RecyclerView = rootView.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2, GridLayoutManager.HORIZONTAL, false)
        adapter = BrandsAdapter(emptyList())

        viewModel = ViewModelProvider(this, HomeViewModelFactory(HomeRepositoryImpl(ShopifyRemoteDataSourceImpl)))
            .get(BrandsViewModel::class.java)

        recyclerView.adapter = adapter

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.brands.observe(viewLifecycleOwner, { brands ->
            adapter.updateBrands(brands)
        })
        viewModel.fetchBrands()
    }
}
