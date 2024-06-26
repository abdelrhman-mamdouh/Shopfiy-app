package com.example.exclusive.ui.auth.login

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController

import com.example.exclusive.R
import com.example.exclusive.databinding.FragmentLoginBinding
import com.example.exclusive.ui.auth.AuthViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    private val viewModel: AuthViewModel by viewModels()
    private var isMainActivityLaunched = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getToken()
        binding.titleBar.tvTitle.text = getString(R.string.login)
        binding.titleBar.icBack.visibility = View.GONE
        binding.continueAsGuest.setOnClickListener {
            viewModel.updateIsGuest(true)
            findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
        }
        lifecycleScope.launch {
            viewModel.tokenState.collect {
                if (it != null && it.isNotEmpty() && !isMainActivityLaunched) {
                    isMainActivityLaunched = true
                    startMainActivity()
                } else if (it == null) {
                    binding.loading.visibility = View.GONE
                    binding.clLogin.visibility = View.VISIBLE
                }
            }
        }

        binding.tvDontHaveAccount.setOnClickListener {
            NavHostFragment.findNavController(this@LoginFragment)
                .navigate(R.id.action_loginFragment_to_signUpFragment)
        }
        binding.tvForgotPassword.setOnClickListener {
            NavHostFragment.findNavController(this@LoginFragment)
                .navigate(R.id.action_loginFragment_to_forgetPasswordFragment)
        }
        binding.btnLogin.setOnClickListener {
            hideKeyboard()
            if (binding.etEmail.text.toString().isEmpty() || binding.etPassword.text.toString()
                    .isEmpty()
            ) {
                Toast.makeText(
                    requireContext(),
                    "Please enter email and password",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }
            binding.progressBar.visibility = View.VISIBLE
            lifecycleScope.launch {
                viewModel.login(
                    binding.etEmail.text.toString(),
                    binding.etPassword.text.toString()
                )
            }
            lifecycleScope.launch {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.loginState
                        .catch {
                            binding.progressBar.visibility = View.INVISIBLE
                            Toast.makeText(
                                requireContext(),
                                "process failed",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        .collect { success ->
                            if (success == 1) {
                                binding.progressBar.visibility = View.INVISIBLE
                                Toast.makeText(
                                    requireContext(),
                                    "Login successful",
                                    Toast.LENGTH_SHORT
                                ).show()
                                if (!isMainActivityLaunched) {
                                    isMainActivityLaunched = true
                                    viewModel.updateIsGuest(false)
                                    startMainActivity()
                                }
                            } else if (success == -1) {
                                binding.progressBar.visibility = View.INVISIBLE
                                Toast.makeText(
                                    requireContext(),
                                    "Login failed",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                }
            }
        }
    }

    private fun startMainActivity() {
        NavHostFragment.findNavController(this@LoginFragment)
            .navigate(R.id.action_loginFragment_to_homeFragment)

    }

    fun Fragment.hideKeyboard() {
        view?.let { activity?.hideKeyboard(it) }
    }

    fun Activity.hideKeyboard(view: View) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}
