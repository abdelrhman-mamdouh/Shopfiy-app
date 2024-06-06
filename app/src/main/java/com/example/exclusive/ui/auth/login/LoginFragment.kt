package com.example.exclusive.ui.auth.login

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.NavHostFragment
import com.example.exclusive.MainActivity
import com.example.exclusive.R
import com.example.exclusive.databinding.FragmentLoginBinding
import com.example.exclusive.ui.auth.AuthViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth:FirebaseAuth
    private val viewModel: AuthViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth =  Firebase.auth
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
        binding.titleBar.icBack.apply {
            visibility = View.GONE
        }

        lifecycleScope.launch {

            viewModel.tokenState

                .collect {

                    if (it != null&&it.isNotEmpty()) {
                        val intent = Intent(requireActivity(), MainActivity::class.java)

                        startActivity(intent)
                        requireActivity().finish()
                    }
                    else if(it==null){
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
                            binding.progressBar.visibility = View.INVISIBLE
                            Toast.makeText(
                                requireContext(),
                                "Login successful",
                                Toast.LENGTH_SHORT
                            ).show()
                            binding.progressBar.visibility = View.INVISIBLE
                            val intent = Intent(context, MainActivity::class.java)
                            startActivity(intent)
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
}