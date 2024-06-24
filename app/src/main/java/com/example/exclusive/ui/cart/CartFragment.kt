package com.example.exclusive.ui.cart

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
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
import kotlinx.coroutines.flow.count
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
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                cartViewModel.isGuest.collect {

                    if (it) {
                        binding.guestMode.visibility = View.VISIBLE
                        binding.tvGoToLogin.visibility = View.VISIBLE
                        binding.rvCart.visibility = View.GONE
                        binding.cardView.visibility = View.GONE
                    }
                }
            }
        }
        binding.tvGoToLogin.setOnClickListener {
            findNavController().navigate(R.id.action_cartFragment_to_loginFragment)
        }

        binding.titleBar.icBack.setOnClickListener {
            findNavController().navigateUp()

        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().navigateUp()
                }
            })
        viewLifecycleOwner.lifecycleScope.launch {
            cartViewModel.cartProductsResponse.collect { uiState ->
                when (uiState) {
                    is UiState.Success -> {
                        binding.lottieAnimationView.visibility = View.GONE
                        if (uiState.data.isEmpty()) {
                            showNoDataFound()
                        } else {
                            binding.rvCart.visibility = View.VISIBLE
                            binding.lottieNoDataAnimationView.visibility = View.GONE
                            cartProductAdapter.submitList(uiState.data)
                            updateTotalPrice()
                        }
                    }
                    is UiState.Error -> {
                        binding.lottieAnimationView.visibility = View.GONE
                        // Handle the error
                        showError(uiState.exception.message)
                    }
                    UiState.Loading -> {
                        showLoading()
                    }
                    UiState.Idle -> {
                        binding.lottieAnimationView.visibility = View.GONE
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            cartViewModel.error.collect { error ->
                error?.let {

                    showError(it)
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

    private fun showError(message: String?) {
        // Display error message to the user
        Toast.makeText(requireContext(), message ?: "Unknown error", Toast.LENGTH_SHORT).show()
    }

    private fun showLoading() {
        binding.lottieAnimationView.visibility = View.VISIBLE
        binding.lottieAnimationView.playAnimation()
    }

    private fun showNoDataFound() {
        binding.rvCart.visibility = View.GONE
        binding.lottieNoDataAnimationView.visibility = View.VISIBLE
        binding.lottieNoDataAnimationView.playAnimation()
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
                        Log.i("TAG", "observeViewModel: ${state.data}")
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