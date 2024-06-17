package com.example.exclusive.ui.auth.signup

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.exclusive.R
import com.example.exclusive.databinding.FragmentSignUpBinding
import com.example.exclusive.ui.auth.AuthViewModel
import com.example.exclusive.utilities.SnackbarUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull

@AndroidEntryPoint
class SignUpFragment : Fragment() {

    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.titleBar.tvTitle.text = getString(R.string.signUp)
        binding.titleBar.icBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        binding.tvAlreadyHaveAccount.setOnClickListener {
            NavHostFragment.findNavController(this@SignUpFragment)
                .navigate(R.id.action_signUpFragment_to_loginFragment)
        }
        binding.continueAsGuest.setOnClickListener {
            viewModel.updateIsGuest(true)
            findNavController().navigate(R.id.action_signUpFragment_to_loginFragment)
        }
        binding.btnSignUp.setOnClickListener {

            hideKeyboard()
            if (binding.etName.text!!.isEmpty() || binding.etEmail.text!!.isEmpty() || binding.etPassword.text!!.isEmpty()) {
                SnackbarUtils.showSnackbar(
                    requireContext(), requireView(), "Please fill all fields"
                )
                return@setOnClickListener
            }
            binding.progressBar.visibility = View.VISIBLE
            lifecycleScope.launch {
                viewModel.signUp(
                    binding.etName.text.toString(),
                    binding.etEmail.text.toString(),
                    binding.etPassword.text.toString()
                )
            }
            lifecycleScope.launch {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    withTimeoutOrNull(5000L) {
                        viewModel.signUpState.catch {
                                binding.progressBar.visibility = View.GONE
                                it.message?.let { it1 ->
                                    SnackbarUtils.showSnackbar(requireContext(), requireView(), it1)
                                }
                            }.collect { success ->
                                if (success == 1) {
                                    binding.progressBar.visibility = View.GONE

                                    SnackbarUtils.showSnackbar(
                                        requireContext(), requireView(), "Sign up successful"
                                    )
                                    NavHostFragment.findNavController(this@SignUpFragment)
                                        .navigate(R.id.action_signUpFragment_to_loginFragment)
                                } else if (success == -1) {
                                    binding.progressBar.visibility = View.GONE

                                    SnackbarUtils.showSnackbar(
                                        requireContext(), requireView(), "Sign up failed"
                                    )
                                }
                            }
                    }
                }
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
