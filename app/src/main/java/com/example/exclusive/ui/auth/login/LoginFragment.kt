package com.example.exclusive.ui.auth.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.NavHostFragment
import com.example.exclusive.R
import com.example.exclusive.databinding.FragmentLoginBinding
import com.example.exclusive.databinding.FragmentSignUpBinding
import com.example.exclusive.screens.auth.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AuthViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvDontHaveAccount.setOnClickListener {
            NavHostFragment.findNavController(this@LoginFragment)
                .navigate(R.id.action_loginFragment_to_signUpFragment)
        }
        binding.tvForgotPassword.setOnClickListener {
            NavHostFragment.findNavController(this@LoginFragment)
                .navigate(R.id.action_loginFragment_to_forgetPasswordFragment)
        }
        binding.btnLogin.setOnClickListener {
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
                        if (success) {
                            Toast.makeText(
                                requireContext(),
                                "Login successful",
                                Toast.LENGTH_SHORT
                            ).show()
                            binding.progressBar.visibility = View.INVISIBLE
                         //   NavHostFragment.findNavController(this@LoginFragment)
                            //    .navigate(R.id.action_loginFragment_to_homeFragment)
                        }


                    }
                }
            }
        }
    }

}