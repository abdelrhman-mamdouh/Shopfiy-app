package com.example.exclusive.ui.orders.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.navigateUp
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.exclusive.R
import com.example.exclusive.data.remote.UiState
import com.example.exclusive.databinding.FragmentOrderBinding
import com.example.exclusive.model.MyOrder
import com.example.exclusive.ui.orders.viewmodel.OrdersViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class OrderFragment : Fragment(), OnOrderClickListener {

    private var _binding: FragmentOrderBinding? = null
    private val binding get() = _binding!!
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: OrderAdapter
    private val viewModel: OrdersViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOrderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.titleBar.icBack.setOnClickListener {
       findNavController().navigate(R.id.action_orderFragment_to_settingsFragment)
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().navigate(R.id.action_orderFragment_to_settingsFragment)
                }
            })
        binding.titleBar.tvTitle.text = getString(R.string.orders)
        recyclerView = binding.rvOrders
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        observeOrders()
    }

    private fun observeOrders() {
        lifecycleScope.launch {
            viewModel.ordersState.collect { state ->
                when (state) {
                    is UiState.Success -> {
                        binding.progressBar.visibility = View.GONE
                        Log.i("observeOrders", "observeOrders: ${state.data}")
                        val orders = state.data
                        adapter = OrderAdapter(orders, this@OrderFragment)
                        recyclerView.adapter = adapter
                        adapter.notifyDataSetChanged()
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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onOrderClick(order: MyOrder) {
        val bundle = Bundle().apply {
            putParcelable("order", order)
        }
        findNavController().navigate(R.id.action_orderFragment_to_orderDetailsFragment, bundle)
    }


}
