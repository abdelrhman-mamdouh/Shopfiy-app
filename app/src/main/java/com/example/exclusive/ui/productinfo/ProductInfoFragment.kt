package com.example.exclusive.ui.productinfo

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.exclusive.R
import com.example.exclusive.databinding.FragmentProductInfoBinding
import com.example.exclusive.model.ProductNode
import com.example.exclusive.model.getRandomNineReviews
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProductInfoFragment : Fragment() {
    lateinit var binding: FragmentProductInfoBinding
    lateinit var product: ProductNode
    lateinit var  varientAdapter: VarientAdapter
    private val viewModel: ProductInfoViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        product = arguments?.getParcelable("product")!!
        Log.d("product info", product.toString())
        binding = FragmentProductInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.isInWatchList(product)
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isWatchList.collect {
                    if(it){
                        binding.addToFavourateIcon.setImageResource(R.drawable.filled_love)
                    }
                    else{
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
        val rating = reviews.map { it.rating }.average()
        binding.tvProductRating.rating = rating.toFloat()
        binding.tvPrice.text = "${product.variants.edges[0].node.priceV2.amount} ${product.variants.edges[0].node.priceV2.currencyCode}"
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
        binding.rvImages.layoutManager =  LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvImages.adapter = imageAdapter
        varientAdapter  = VarientAdapter(::onSelectListner, product.variants.edges.map{ it.node.title },0)
        binding.tvVariants.layoutManager =  LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.tvVariants.adapter = varientAdapter
        imageAdapter.submitList(imageList)
        varientAdapter.submitList(product.variants.edges.map{ it.node.title })
        binding.addToFavourateIcon.setOnClickListener {
            if (!viewModel.isWatchList.value) {
                binding.addToFavourateIcon.setImageResource(R.drawable.filled_love)
                viewModel.addProductToRealtimeDatabase(product)
            } else {
               val onClickOk ={
                   binding.addToFavourateIcon.setImageResource(R.drawable.empty_love)
                   Log.d("idddddd", product.id)
                   viewModel.removeProductFromWatchList(product.id.substring(22))

               }
                val onClickCancel={}
                val dialog = DailogFramgent(onClickOk,onClickCancel)
                dialog.show(requireActivity().supportFragmentManager, "dialog")
            }


        }
        binding.btnAddToCart.setOnClickListener {
            findNavController().navigate(R.id.action_productInfoFragment_to_watchlistFragment)
        }
    }
    fun onSelectListner(item:String,index:Int){
        varientAdapter.index = index
//        val varientAdapter = VarientAdapter(::onSelectListner, product.variants.edges.map{ it.node.title },index)
//        binding.tvVariants.layoutManager =  LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
//        binding.tvVariants.adapter = varientAdapter
//        varientAdapter.submitList(product.variants.edges.map{ it.node.title })
    }
    }