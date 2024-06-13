package com.example.exclusive

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.exclusive.R
import com.example.exclusive.utilities.SnackbarUtils

// CheckoutWebViewActivity.kt


class CheckoutWebViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout_web_view)

        val webView: WebView = findViewById(R.id.checkoutWebView)
        webView.settings.javaScriptEnabled = true

        val checkoutUrl = intent.getStringExtra("CHECKOUT_URL")
        checkoutUrl?.let {
            webView.loadUrl(it)
        }

        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                if (url != null) {
                    if (url.endsWith("thank-you")) {
                        navigateToThankYouFragment()
                    }
                }
            }

            override fun onReceivedError(
                view: WebView?, errorCode: Int,
                description: String?, failingUrl: String?
            ) {
                super.onReceivedError(view, errorCode, description, failingUrl)
                Toast.makeText(this@CheckoutWebViewActivity, "Error: $description", Toast.LENGTH_LONG).show()
            }

            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                Toast.makeText(this@CheckoutWebViewActivity, "thank-youUp", Toast.LENGTH_LONG).show()
                if (url != null) {
                    if (url.endsWith("thank-you")) {
                        navigateToThankYouFragment()
                        Toast.makeText(this@CheckoutWebViewActivity, "thank-youDown", Toast.LENGTH_LONG).show()
                        return true
                    } else if (url.startsWith("http://") || url.startsWith("https://")) {
                        view?.loadUrl(url)
                        return true
                    } else {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                        startActivity(intent)
                        return true
                    }
                }
                return false
            }
        }
    }

    private fun navigateToThankYouFragment() {
        val intent = Intent(this, HolderActivity::class.java).apply {
            putExtra(HolderActivity.GO_TO, "SUCCESS")
        }
        startActivity(intent)
        finish()
    }
}
