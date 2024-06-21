package com.example.exclusive.ui.orderdetails.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.exclusive.R
import com.example.exclusive.databinding.FragmentOrderDetailsBinding
import com.example.exclusive.data.model.MyOrder

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
        val order: MyOrder? = arguments?.getParcelable("order")
        binding.tvFirstName.text = "Name: "+order?.billingAddress?.firstName.toString()
        binding.tvAddress.text = "Address: "+order?.billingAddress?.address1.toString()
        binding.tvPhone.text ="Phone: "+ order?.billingAddress?.phone.toString()
        setupRecyclerView(order!!)
        binding.titleBar.icBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    private fun setupRecyclerView(order: MyOrder) {

        adapter = OrderDetailsAdapter(order.lineItems)
        binding.rvOrders.layoutManager = LinearLayoutManager(requireContext())
        binding.rvOrders.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
