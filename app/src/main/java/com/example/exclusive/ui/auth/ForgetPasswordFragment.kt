package com.example.exclusive.ui.auth

import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.exclusive.R
import com.example.exclusive.databinding.FragmentForgetPasswordBinding
import com.example.exclusive.utilities.SnackbarUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ForgetPasswordFragment : Fragment() {

    private var _binding: FragmentForgetPasswordBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AuthViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentForgetPasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.titleBar.tvTitle.text = getString(R.string.forgetPasswordTitle)
        binding.titleBar.icBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
        binding.btnSend.setOnClickListener {
            binding.progressBar.visibility = View.VISIBLE
            hideKeyboard()
            lifecycleScope.launch {
                viewModel.sendPassword(binding.etEmail.text.toString())
                binding.progressBar.visibility = View.GONE
                SnackbarUtils.showSnackbar(requireContext(),requireView(),"Check your email")
                parentFragmentManager.popBackStack()
            }
        }
    }
    fun Fragment.hideKeyboard() {
        view?.let { activity?.hideKeyboard(it) }
    }

    fun Activity.hideKeyboard(view: View) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

}