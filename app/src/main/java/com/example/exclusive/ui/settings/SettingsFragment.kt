package com.example.exclusive.ui.settings

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.exclusive.HolderActivity
import com.example.exclusive.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvAddress.setOnClickListener {
            val intent = Intent(requireContext(), HolderActivity::class.java).apply {
                putExtra(HolderActivity.GO_TO, "ADDRESS")
            }
            startActivity(intent)
        }

        binding.cvCurrency.setOnClickListener {
            val intent = Intent(requireContext(), HolderActivity::class.java).apply {
                putExtra(HolderActivity.GO_TO, "CURRENCY")
            }
            startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}