package com.example.exclusive.ui.productinfo

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.exclusive.R
import com.example.exclusive.data.remote.UiState
import com.example.exclusive.databinding.FragmentProductInfoBinding
import com.example.exclusive.model.ProductNode
import com.example.exclusive.model.Variant
import com.example.exclusive.model.VariantNode
import com.example.exclusive.model.getRandomNineReviews
import com.example.exclusive.ui.products.viewmodel.ProductInfoViewModel
import com.example.exclusive.utilities.SnackbarUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class ProductInfoFragment : Fragment() {
    lateinit var binding: FragmentProductInfoBinding
    lateinit var product: ProductNode
    lateinit var varientAdapter: VarientAdapter
    lateinit var choosenVarient: VariantNode
    private val viewModel: ProductInfoViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        product = arguments?.getParcelable("product")!!
        Log.d("product info", product.toString())
        binding = FragmentProductInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.isInWatchList(product)
        choosenVarient=product.variants.edges[0].node
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isWatchList.collect {
                    if (it) {
                        binding.addToFavourateIcon.setImageResource(R.drawable.filled_love)
                    } else {
                        binding.addToFavourateIcon.setImageResource(R.drawable.empty_love)
                    }
                }
            }
        }
        binding.titleBar.tvTitle.text = getString(R.string.product_info)
        binding.titleBar.icBack.setOnClickListener {
            this.requireActivity().onBackPressed()
        }
        binding.tvProductName.text = product.title
        binding.tvProductBrand.text = product.vendor
        binding.tvProductDescription.text = product.description
        val reviews = getRandomNineReviews()

        binding.tvProductRating.rating = product.rating
        Log.d("ratingg", product.rating.toString())
        binding.tvPrice.text =
            "${product.variants.edges[0].node.priceV2.amount} ${product.variants.edges[0].node.priceV2.currencyCode}"
        binding.tvReviewerName1.text = reviews[0].name
        binding.rbReviewerRating1.rating = reviews[0].rating.toFloat()
        binding.tvReviewerComment1.text = reviews[0].comment

        binding.tvReviewerName2.text = reviews[1].name
        binding.rbReviewerRating2.rating = reviews[1].rating.toFloat()
        binding.tvReviewerComment2.text = reviews[1].comment

        binding.tvReviewerName3.text = reviews[2].name
        binding.rbReviewerRating3.rating = reviews[2].rating.toFloat()
        binding.tvReviewerComment3.text = reviews[2].comment
        val imageList = product.images.edges.map { it.node.src }

        val imageAdapter = ImageAdapter(imageList)
        binding.rvImages.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvImages.adapter = imageAdapter
        varientAdapter =
            VarientAdapter(::onSelectListner, product.variants.edges.map { it.node.title }, 0)
        binding.tvVariants.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.tvVariants.adapter = varientAdapter
        imageAdapter.submitList(imageList)
        varientAdapter.submitList(product.variants.edges.map { it.node.title })

        binding.addToFavourateIcon.setOnClickListener {
            if (!viewModel.isWatchList.value) {
                binding.addToFavourateIcon.setImageResource(R.drawable.filled_love)
                viewModel.addProductToRealtimeDatabase(product)
                SnackbarUtils.showSnackbar(requireContext(), view, "Product added to favorites")
            } else {
                val onClickOk = {
                    binding.addToFavourateIcon.setImageResource(R.drawable.empty_love)
                    Log.d("idddddd", product.id)
                    viewModel.removeProductFromWatchList(product.id.substring(22))
                    SnackbarUtils.showSnackbar(requireContext(), view, "Product removed from favorites")
                }
                val onClickCancel = {}
                val dialog = DailogFramgent(onClickOk, onClickCancel)
                dialog.show(requireActivity().supportFragmentManager, "dialog")
            }
        }



        binding.btnAddToCart.setOnClickListener {
            viewModel.addToCart(product.variants.edges[0].node.id,product.variants.edges[0].node.quantityAvailable)
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.addToCartState.collect { uiState ->
                when (uiState) {
                    is UiState.Success -> {
                        SnackbarUtils.showSnackbar(requireContext(), view, "Product added to cart")
                    }

                    is UiState.Error -> {
                        SnackbarUtils.showSnackbar(
                            requireContext(), binding.root, "Failed to add to cart"
                        )
                    }

                    else -> {

                    }
                }
            }
        }

    }

    fun onSelectListner(item: String, index: Int) {
        varientAdapter.index = index
        binding.tvPrice.text="${product.variants.edges[index].node.priceV2.amount} ${product.variants.edges[index].node.priceV2.currencyCode}"
        choosenVarient=product.variants.edges[index].node
        varientAdapter.notifyDataSetChanged()
    }
}