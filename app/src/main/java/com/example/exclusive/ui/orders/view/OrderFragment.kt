package com.example.exclusive.ui.orders.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.exclusive.R
import com.example.exclusive.databinding.FragmentOrderBinding
import com.example.exclusive.model.OrderItem


class OrderFragment : Fragment(), OnOrderClickListener {

    private var _binding: FragmentOrderBinding? = null
    private val binding get() = _binding!!
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: OrderAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOrderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.titleBar.tvTitle.text = getString(R.string.orders)
        recyclerView = binding.rvOrders
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val dummyDataList = listOf(
            OrderItem("Order 1", "05-12-2019", "IW3475453455", 3, "112$", "Delivered"),
            OrderItem("Order 2", "06-12-2019", "IW3475453456", 2, "90$", "In Progress"),
            OrderItem("Order 3", "07-12-2019", "IW3475453457", 4, "150$", "Shipped")
        )
        adapter = OrderAdapter(dummyDataList,this)
        recyclerView.adapter = adapter
        binding.titleBar.icBack.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onOrderClick(order: OrderItem) {
        val bundle = Bundle()
        bundle.putParcelable("order", order)

        findNavController().navigate(R.id.action_orderFragment_to_orderDetailsFragment, bundle)
    }
}