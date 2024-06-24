package com.example.exclusive.ui.productinfo

import android.graphics.Paint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.exclusive.R
import com.example.exclusive.data.remote.UiState
import com.example.exclusive.databinding.FragmentProductInfoBinding
import com.example.exclusive.model.ProductNode
import com.example.exclusive.model.VariantNode
import com.example.exclusive.model.getRandomNineReviews
import com.example.exclusive.ui.productinfo.viewmodel.ProductInfoViewModel
import com.example.exclusive.utilities.Constants.showConfirmationDialog
import com.example.exclusive.utilities.SnackbarUtils
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProductInfoFragment : Fragment() {
    private lateinit var binding: FragmentProductInfoBinding
    private lateinit var product: ProductNode
    private lateinit var variantAdapter: VariantAdapter
    private lateinit var chosenVariant: VariantNode
    private lateinit var imageAdapter: ImageAdapter
    private lateinit var viewPager: ViewPager2

    private lateinit var dotsIndicator: DotsIndicator
    private val viewModel: ProductInfoViewModel by viewModels()
    private val handler = Handler(Looper.getMainLooper())
    private var currentPage = 0
    private var isAutoScrolling = false
    private var autoScrollRunnable: Runnable? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        product = arguments?.getParcelable("product")!!
        binding = FragmentProductInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        setupTitleBar()
        setupBackButton()
        setupProductInfo()
        setupReviews()
        setupImageSlider()
        setupVariants()
        setupAddToFavorites()
        setupAddToCart()
        observeAddToCartState()
    }

    private fun setupTitleBar() {

        viewModel.isInWatchList(product)
        chosenVariant = product.variants.edges[0].node
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isWatchList.collect {
                    if (it) {
                        binding.addToFavouriteIcon.setImageResource(R.drawable.filled_love)
                    } else {
                        binding.addToFavouriteIcon.setImageResource(R.drawable.empty_love)
                    }
                }
            }
        }

        binding.titleBar.tvTitle.text = getString(R.string.product_info)
    }

    private fun setupBackButton() {
        binding.titleBar.icBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    requireActivity().supportFragmentManager.popBackStack()
                }
            })
    }

    private fun setupProductInfo() {
        binding.tvProductName.text = product.title
        binding.tvProductBrand.text = product.vendor
        binding.tvProductDescription.text = product.description
        binding.tvProductRating.rating = product.rating.toFloat()
        binding.tvPrice.text =
            "${product.variants.edges[0].node.priceV2.amount} ${product.variants.edges[0].node.priceV2.currencyCode}"
    }

    private fun setupReviews() {
        val reviews = getRandomNineReviews()

        binding.txtViewReviewerName.text = reviews[0].name
        binding.reviewRate.rating = reviews[0].rating.toFloat()
        binding.reviewerComment.text = reviews[0].comment

        binding.txtViewReviewerName2.text = reviews[1].name
        binding.reviewRate2.rating = reviews[1].rating.toFloat()
        binding.reviewerComment2.text = reviews[1].comment

        binding.txtViewReviewerName3.text = reviews[2].name
        binding.reviewRate3.rating = reviews[2].rating.toFloat()
        binding.reviewerComment3.text = reviews[2].comment
    }

    private fun setupImageSlider() {
        val imageList = product.images.edges.map { it.node.src }
        imageAdapter = ImageAdapter(imageList)
        viewPager = binding.viewPager
        viewPager.adapter = imageAdapter
        dotsIndicator = binding.dotsIndicator
        dotsIndicator.setViewPager2(viewPager)

        // Start auto-scrolling
        startAutoScroll()
    }

    private fun startAutoScroll() {
        if (!isAutoScrolling && imageAdapter.itemCount > 1) {
            autoScrollRunnable = Runnable {
                currentPage = viewPager.currentItem + 1
                if (currentPage >= imageAdapter.itemCount) {
                    currentPage = 0
                }
                viewPager.setCurrentItem(currentPage, true)
                handler.postDelayed(autoScrollRunnable!!, AUTO_SCROLL_DELAY)
            }
            handler.postDelayed(autoScrollRunnable!!, AUTO_SCROLL_DELAY)
            isAutoScrolling = true
        }
    }

    private fun stopAutoScroll() {
        handler.removeCallbacks(autoScrollRunnable!!)
        isAutoScrolling = false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        stopAutoScroll()
    }

    private fun setupVariants() {
        val variantTitles = product.variants.edges.map { it.node.title }
        variantAdapter = VariantAdapter(::onVariantSelected, variantTitles)
        binding.tvVariants.adapter = variantAdapter
        binding.tvVariants.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        onVariantSelected(variantTitles[0],0)
    }

    private fun setupAddToFavorites() {
        binding.addToFavouriteIcon.setOnClickListener {
            handleAddToFavoritesClick()
        }
        viewModel.isInWatchList(product)
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isWatchList.collect {
                    if (it) {
                        binding.addToFavouriteIcon.setImageResource(R.drawable.filled_love)
                    } else {
                        binding.addToFavouriteIcon.setImageResource(R.drawable.empty_love)
                    }
                }
            }
        }

    }

    private fun setupAddToCart() {

        if(chosenVariant.quantityAvailable == 0){
            binding.btnAddToCart.isEnabled = false
            binding.btnAddToCart.text = "Out of Stock"
            binding.btnAddToCart.paintFlags = binding.btnAddToCart.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            binding.btnAddToCart.setTextColor(ContextCompat.getColor(requireContext(), R.color.red))
        }

        binding.btnAddToCart.setOnClickListener {
            handleAddToCartClick()
        }
    }

    private fun observeAddToCartState() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.addToCartState.collect { uiState ->
                when (uiState) {
                    is UiState.Success -> {
                        SnackbarUtils.showSnackbar(requireContext(), binding.root, "Product added to cart")
                    }
                    is UiState.Error -> {
                        SnackbarUtils.showSnackbar(requireContext(), binding.root, "Failed to add to cart")
                    }
                    else -> {}
                }
            }
        }
    }

    private fun handleAddToFavoritesClick() {
        lifecycleScope.launch {
            viewModel.isGuest.collect {
                if (it) {
                    showConfirmationDialog(
                        requireContext(),
                        R.drawable.gif1,
                        title = "You are in guest mode\n",
                        message = "Do you want to login?",
                        positiveButtonText = "Yes",
                        onPositiveClick = {
                            findNavController().navigate(R.id.action_productInfoFragment_to_loginFragment)
                        })
                } else {
                    if (!viewModel.isWatchList.value) {
                        binding.addToFavouriteIcon.setImageResource(R.drawable.filled_love)
                        viewModel.addProductToRealtimeDatabase(product)
                        SnackbarUtils.showSnackbar(
                            requireContext(), binding.root, "Product added to favorites"
                        )
                    } else {
                        showRemoveFromFavoritesDialog(R.drawable.gif)
                    }
                }
            }
        }
    }

    private fun handleAddToCartClick() {
        lifecycleScope.launch {
            viewModel.isGuest.collect {
                if (it) {
                    showConfirmationDialog(
                        requireContext(),
                        R.drawable.gif1,
                        title = "You are in guest mode\n",
                        message = "Do you want to login?",
                        positiveButtonText = "Yes",
                        onPositiveClick = {
                            findNavController().navigate(R.id.action_productInfoFragment_to_loginFragment)
                        })
                } else {
                    viewModel.addToCart(chosenVariant.id, chosenVariant.quantityAvailable)
                }
            }
        }
    }

    private fun showRemoveFromFavoritesDialog(gif: Int) {
        showConfirmationDialog(
            requireContext(),
            gif =gif,
            title = "Remove from Favorites",
            message = "Are you sure you want to remove this product from favorites?",
            positiveButtonText = "Yes",
            onPositiveClick = {
                binding.addToFavouriteIcon.setImageResource(R.drawable.empty_love)
                viewModel.removeProductFromWatchList(product.id.substring(22))
                SnackbarUtils.showSnackbar(
                    requireContext(), binding.root, "Product removed from favorites"
                )
            })
    }

    private fun onVariantSelected(variant: String, position: Int) {
        chosenVariant = product.variants.edges[position].node
        Log.d("VariantSelected", "Variant: $variant, Position: $position")
    }

    companion object {
        private const val AUTO_SCROLL_DELAY = 3000L // Auto-scroll delay in milliseconds
    }
}
