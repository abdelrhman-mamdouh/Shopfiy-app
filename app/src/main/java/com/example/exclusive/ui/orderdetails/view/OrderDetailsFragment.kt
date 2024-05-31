package com.example.exclusive.ui.orderdetails.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.exclusive.R
import com.example.exclusive.databinding.FragmentOrderDetailsBinding
import com.example.exclusive.model.ProductItem

class OrderDetailsFragment : Fragment() {

    private var _binding: FragmentOrderDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: OrderDetailsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOrderDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.titleBar.tvTitle.text = getString(R.string.orderDetails)
        setupRecyclerView()
        binding.titleBar.icBack.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    private fun setupRecyclerView() {
        val productList = listOf(
            ProductItem(
                title = "Product 1",
                color = "Color: Red",
                size = "Size: M",
                units = "10 units",
                price = "$20",
                imageUrl = "https://via.placeholder.com/150"
            ),
            ProductItem(
                title = "Product 2",
                color = "Color: Blue",
                size = "Size: L",
                units = "5 units",
                price = "$25",
                imageUrl = "https://via.placeholder.com/150"
            ),
            ProductItem(
                title = "Product 3",
                color = "Color: Green",
                size = "Size: S",
                units = "15 units",
                price = "$30",
                imageUrl = "https://via.placeholder.com/150"
            )
        )
        adapter = OrderDetailsAdapter(productList)
        binding.rvOrders.layoutManager = LinearLayoutManager(requireContext())
        binding.rvOrders.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
