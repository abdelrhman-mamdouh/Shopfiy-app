package com.example.exclusive.ui.checkout.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.exclusive.R

class CheckoutWebViewFragment : Fragment() {

    private val args: CheckoutWebViewFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_web_view_checkout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val webView: WebView = view.findViewById(R.id.checkoutWebView)
        webView.settings.javaScriptEnabled = true

        val checkoutUrl = args.webUrl
        webView.loadUrl(checkoutUrl)

        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                if (url != null && url.endsWith("thank-you")) {
                    navigateToThankYouFragment()
                }
            }

            override fun onReceivedError(
                view: WebView?, errorCode: Int,
                description: String?, failingUrl: String?
            ) {
                super.onReceivedError(view, errorCode, description, failingUrl)
                Toast.makeText(requireContext(), "Error: $description", Toast.LENGTH_LONG).show()
            }

            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                if (url != null) {
                    return if (url.endsWith("thank-you")) {
                        navigateToThankYouFragment()
                        true
                    } else if (url.startsWith("http://") || url.startsWith("https://")) {
                        view?.loadUrl(url)
                        true
                    } else {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                        startActivity(intent)
                        true
                    }
                }
                return false
            }
        }
    }

    private fun navigateToThankYouFragment() {
        findNavController().navigate(R.id.action_checkoutWebViewFragment_to_paymentCompletedFragment)
    }
}
