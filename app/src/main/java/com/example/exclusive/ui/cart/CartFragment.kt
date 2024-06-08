package com.example.exclusive.ui.cart

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.exclusive.R
import com.example.exclusive.data.remote.UiState
import com.example.exclusive.databinding.FragmentCartBinding
import com.example.exclusive.model.CartProduct
import com.example.exclusive.type.CheckoutLineItemInput
import com.example.exclusive.utilities.SnackbarUtils
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CartFragment : Fragment(), CartProductAdapter.OnQuantityChangeListener {

    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding!!

    private val cartViewModel: CartViewModel by viewModels()

    private lateinit var cartProductAdapter: CartProductAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.titleBar.tvTitle.text = getString(R.string.cart)
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    requireActivity().finish()
                }
            })
        binding.titleBar.icBack.setOnClickListener {
            requireActivity().finish()
        }



        viewLifecycleOwner.lifecycleScope.launch {
            cartViewModel.cartProductsResponse.collect { response ->
                if (response != null) {
                    cartProductAdapter.submitList(response)
                    updateTotalPrice()
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            cartViewModel.error.collect { error ->
                if (error != null) {

                }
            }
        }

        binding.buttonCheckout.setOnClickListener {
            val lineItems = getCheckoutLineItems()
            cartViewModel.createCheckout(lineItems)
        }
        setupRecyclerView()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        cartProductAdapter = CartProductAdapter(this)
        binding.rvCart.apply {
            adapter = cartProductAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
        setupSwipeToDelete()
    }

    private fun setupSwipeToDelete() {
        val itemTouchHelperCallback = object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val removedItem = cartProductAdapter.removeItem(position)
                Snackbar.make(binding.root, "Product removed from cart", Snackbar.LENGTH_LONG)
                    .setAction("UNDO") {
                        removedItem?.let {
                            cartProductAdapter.addItem(position, it)
                        }
                    }.addCallback(object : Snackbar.Callback() {
                        override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                            if (event != Snackbar.Callback.DISMISS_EVENT_ACTION) {
                                removedItem?.let {
                                    viewLifecycleOwner.lifecycleScope.launch {
                                        cartViewModel.deleteProductFromCart(it)
                                    }
                                }
                            }
                        }
                    }).show()
            }
        }
        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(binding.rvCart)
    }

    private fun showUndoSnackbar(product: CartProduct) {
        val position = cartProductAdapter.currentList.indexOf(product)
        cartProductAdapter.removeItem(position)
        Snackbar.make(binding.root, "Product removed from cart", Snackbar.LENGTH_LONG)
            .setAction("UNDO") {
                cartProductAdapter.addItem(position, product)
            }.addCallback(object : Snackbar.Callback() {
                override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                    if (event != Snackbar.Callback.DISMISS_EVENT_ACTION) {
                        viewLifecycleOwner.lifecycleScope.launch {
                            cartViewModel.deleteProductFromCart(product)
                        }
                    }
                }
            }).show()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    override fun onRemoveProduct(product: CartProduct) {
        showUndoSnackbar(product)
        updateTotalPrice()
    }

    override fun onQuantityChanged() {
        updateTotalPrice()
    }

    private fun calculateTotalPrice(): Double {
        return cartProductAdapter.currentList.sumOf { product ->
            val quantity = cartProductAdapter.getCurrentQuantity(product.id)
            quantity * product.variantPrice.toDouble()
        }
    }

    private fun updateTotalPrice() {
        val totalPrice = calculateTotalPrice()
        binding.textViewTotalPrice.text = String.format("%.2f", totalPrice)
    }
    private fun getCheckoutLineItems(): List<CheckoutLineItemInput> {
        return cartProductAdapter.currentList.map { product ->
            val quantity = cartProductAdapter.getCurrentQuantity(product.id)
            CheckoutLineItemInput(
                quantity = quantity,
                variantId = product.variantId
            )
        }
    }
    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            cartViewModel.checkoutState.collect { state ->
                when (state) {
                    is UiState.Loading -> {

                    }
                    is UiState.Success -> {
                        SnackbarUtils.showSnackbar(requireContext(),requireView(),"Checkout Created")
                        findNavController().navigate(R.id.action_cartFragment_to_checkoutFragment)
                    }
                    is UiState.Error -> {
                        Toast.makeText(requireContext(), state.exception.message, Toast.LENGTH_SHORT).show()
                    }
                    UiState.Idle -> {

                    }
                }
            }
        }
    }
}