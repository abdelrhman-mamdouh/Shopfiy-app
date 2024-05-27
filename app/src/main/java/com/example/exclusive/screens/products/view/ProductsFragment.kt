package com.example.exclusive.screens.products.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RadioGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.exclusive.R
import com.example.exclusive.data.remote.ShopifyRemoteDataSourceImpl
import com.example.exclusive.model.MyProduct
import com.example.exclusive.screens.products.repository.ProductsRepositoryImpl
import com.example.exclusive.screens.products.viewmodel.ProductsViewModel
import com.example.exclusive.screens.products.viewmodel.ProductsViewModelFactory



import com.google.android.material.slider.RangeSlider

class ProductsFragment : Fragment(), OnProductClickListener {


    private lateinit var viewModel: ProductsViewModel
    private lateinit var adapter: ProductsAdapter
    private lateinit var radioGroup: RadioGroup
    private lateinit var minValueTextView: TextView
    private lateinit var maxValueTextView: TextView
    private lateinit var rangeSlider: RangeSlider

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_products, container, false)
        val recyclerView: RecyclerView = rootView.findViewById(R.id.rv_products)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        adapter = ProductsAdapter(emptyList(), this)

        viewModel = ViewModelProvider(
            this, ProductsViewModelFactory(
                ProductsRepositoryImpl(
                    ShopifyRemoteDataSourceImpl
                )
            )
        ).get(ProductsViewModel::class.java)

        recyclerView.adapter = adapter

        val backButton: ImageView = rootView.findViewById(R.id.ic_back)
        backButton.setOnClickListener {
            requireActivity().onBackPressed()
        }

        radioGroup = rootView.findViewById(R.id.subCategoryRadioGroup)
        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            val selectedCategory = when (checkedId) {
                R.id.radioBtnAccessories -> getString(R.string.accessories)
                R.id.radioBtnTShirts -> getString(R.string.t_shirts)
                R.id.radioBtnShoes -> getString(R.string.shoes)
                else -> getString(R.string.all)
            }
            adapter.filterProducts(selectedCategory)
        }

        minValueTextView = rootView.findViewById(R.id.minValue)
        maxValueTextView = rootView.findViewById(R.id.maxValue)
        rangeSlider = rootView.findViewById(R.id.brandProductsRangedSeekbar)
        rangeSlider.addOnChangeListener { slider, _, _ ->
            val values = slider.values
            val minValue = values[0].toInt()
            val maxValue = values[1].toInt()
            minValueTextView.text = "$minValue EGP"
            maxValueTextView.text = "$maxValue EGP"
            adapter.filterByPrice(minValue, maxValue)
            adapter.notifyDataSetChanged()
        }
        adapter.notifyDataSetChanged()
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val intent = activity?.intent
        val brandName = intent?.getStringExtra("brand_name")
        brandName?.let { viewModel.fetchProducts(it) }
        viewModel.products.observe(viewLifecycleOwner, { products ->
            adapter.updateProducts(products)
        })
    }

    override fun onProductClick(product: MyProduct) {
        // Handle product click event
        // For example, navigate to product details screen
    }
}
