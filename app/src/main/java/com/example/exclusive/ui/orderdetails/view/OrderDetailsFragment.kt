package com.example.exclusive.ui.orderdetails.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.exclusive.R
import com.example.exclusive.databinding.FragmentOrderDetailsBinding
import com.example.exclusive.model.MyOrder
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
        val order: MyOrder? = arguments?.getParcelable("order")
        Log.i("MyOrder", "onViewCreated: ${order}")
        binding.tvFirstName.text = "Name: "+order?.billingAddress?.first_name.toString()
        binding.tvAddress.text = "Address: "+order?.billingAddress?.address1.toString()
        if(order?.billingAddress?.phone.toString()==null){
            binding.tvPhone.visibility = View.GONE
        }else{
            binding.tvPhone.text ="Phone: "+ order?.billingAddress?.phone.toString()
        }
        setupRecyclerView(order!!)
        binding.titleBar.icBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack();
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    requireActivity().supportFragmentManager.popBackStack();
                }
            })
    }

    private fun setupRecyclerView(order:MyOrder) {

        adapter = OrderDetailsAdapter(order.lineItems)
        binding.rvOrders.layoutManager = LinearLayoutManager(requireContext())
        binding.rvOrders.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
