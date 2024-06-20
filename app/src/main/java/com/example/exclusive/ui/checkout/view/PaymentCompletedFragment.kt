package com.example.exclusive.ui.checkout.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.exclusive.R
import com.example.exclusive.databinding.FragmentPaymentCompletedBinding

class PaymentCompletedFragment : Fragment() {

    private var _binding: FragmentPaymentCompletedBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPaymentCompletedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.titleBar.tvTitle.text = getString(R.string.payment_success)
        binding.titleBar.icBack.visibility = View.GONE

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {

                }
            })
        binding.btnContinueShopping.setOnClickListener {

            val currentDestination = findNavController().currentDestination
            if (currentDestination?.id == R.id.paymentCompletedFragment) {
                Log.e("TAG", "Current destination is paymentCompletedFragment")
                findNavController().navigate(R.id.action_paymentCompletedFragment_to_orderFragment)
            } else {
                Log.e("TAG", "Current destination is not paymentCompletedFragment")
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
