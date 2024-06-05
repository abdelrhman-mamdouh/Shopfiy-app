package com.example.exclusive.ui.discountads
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import com.example.exclusive.R
import com.example.exclusive.data.model.PriceRuleSummary
import com.example.exclusive.databinding.FragmentDicountBinding
import com.example.exclusive.utilities.SharedPreferencesManager


class DiscountFragment : Fragment() {
    private var _binding: FragmentDicountBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDicountBinding.inflate(inflater, container, false)
        val view = binding.root
        val priceRuleSummary = SharedPreferencesManager.getPriceRule(requireContext())
        populateView(priceRuleSummary)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.titleBar.tvTitle.text = getString(R.string.coupon)
        binding.titleBar.icBack.setOnClickListener {
            requireActivity().onBackPressed()
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                requireActivity().finish()
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun populateView(priceRuleSummary: PriceRuleSummary?) {
        priceRuleSummary?.let {
            binding.tvType.text = it.valueType
            binding.tvDicsounValue.text = "${it.value}%"
            binding.tvDiscountSelection.text = it.customerSelection
            binding.tvDiscountLimit.text = it.usageLimit?.toString() ?: "Unlimited"
            binding.tvDiscountDate.text = "Valid from ${it.startsAt} to ${it.endsAt}"
        }
    }
}
