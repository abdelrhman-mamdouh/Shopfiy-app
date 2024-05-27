package com.example.exclusive.screens.category.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.exclusive.HolderActivity
import com.example.exclusive.R
import com.example.exclusive.data.remote.ShopifyRemoteDataSourceImpl
import com.example.exclusive.model.Brand
import com.example.exclusive.screens.category.repository.CategoriesRepositoryImpl
import com.example.exclusive.screens.category.viewmodel.CategoryViewModel
import com.example.exclusive.screens.category.viewmodel.CategoryViewModelFactory
import com.example.exclusive.screens.home.viewmodel.BrandsViewModel


class CategoryFragment : Fragment(), OnCategoryClickListener {

    private lateinit var viewModel: CategoryViewModel
    private lateinit var adapter: CategoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_category, container, false)
        val recyclerView: RecyclerView = rootView.findViewById(R.id.recyclerView)
        recyclerView.layoutManager =
            GridLayoutManager(requireContext(), 2, GridLayoutManager.HORIZONTAL, false)
        adapter = CategoryAdapter(emptyList(), this)

        viewModel = ViewModelProvider(
            this, CategoryViewModelFactory(
                CategoriesRepositoryImpl(
                    ShopifyRemoteDataSourceImpl
                )
            )
        ).get(CategoryViewModel::class.java)

        recyclerView.adapter = adapter

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.categories.observe(viewLifecycleOwner, { brands ->
            adapter.updateCategory(brands)
        })
        viewModel.fetchCategories()
    }

    override fun onCategoryClick(brand: Brand) {
        val intent = Intent(context, HolderActivity::class.java)
        intent.putExtra("brand_name", brand.name)
        startActivity(intent)
    }
}

