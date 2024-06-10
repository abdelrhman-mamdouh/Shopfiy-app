package com.example.exclusive.ui.checkout.view

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.exclusive.utilities.SnackbarUtils
import com.shopify.checkoutsheetkit.CheckoutException
import com.shopify.checkoutsheetkit.DefaultCheckoutEventProcessor
import com.shopify.checkoutsheetkit.HttpException
import com.shopify.checkoutsheetkit.LogWrapper
import com.shopify.checkoutsheetkit.lifecycleevents.CheckoutCompletedEvent

class CustomCheckoutEventProcessor(
    private val context: Context,
    private val view: View,
    log: LogWrapper = LogWrapper()
) : DefaultCheckoutEventProcessor(context, log) {

    override fun onCheckoutCanceled() {
        SnackbarUtils.showSnackbar(
            context, view, "Checkout was canceled"
        )
    }

    override fun onCheckoutCompleted(checkoutCompletedEvent: CheckoutCompletedEvent) {
        SnackbarUtils.showSnackbar(
            context, view, "Checkout completed successfully"
        )
    }

    override fun onCheckoutFailed(error: CheckoutException) {
        val errorMessage = buildErrorMessage(error)
        Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
        SnackbarUtils.showSnackbar(
            context, view, errorMessage
        )
    }

    private fun buildErrorMessage(error: CheckoutException): String {
        val errorMessage = StringBuilder().apply {
            append("Checkout failed: ${error.message}")
            error.cause?.let { append("\nCause: $it") }
            if (error is HttpException) {
                append("\nHTTP Error Message: ${error.message}")
            }
            append("\nStack Trace: ${Log.getStackTraceString(error)}")
        }.toString()
        Log.i("buildErrorMessage", "buildErrorMessage:${errorMessage} ")
        return errorMessage
    }

    override fun onCheckoutLinkClicked(uri: Uri) {
        val intent = Intent(Intent.ACTION_VIEW, uri)
        context.startActivity(intent)
    }

}
