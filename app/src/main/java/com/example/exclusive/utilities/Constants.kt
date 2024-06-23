package com.example.exclusive.utilities

import android.app.Activity
import android.app.Dialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.Window
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.exclusive.R
import com.example.exclusive.data.model.DiscountCode
import com.example.exclusive.data.model.PriceRuleSummary
import com.example.exclusive.databinding.DialogCouponDetailBinding

object Constants {
    const val GOOGLE_KEY: String = "AIzaSyCzL-KKINpE6at4j4zH8qNXAB6eMn8Y0L0"
    const val BASE_URL: String = "https://mad44-android-sv-2.myshopify.com/api/2024-04/graphql.json"
    const val CURRENCY_BASE_URL: String = "https://api.fastforex.io/"
    const val API_KEY: String = "43f6205d2d0b257b652e16f5f7663414"
    const val ACCESS_TOKEN: String = "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"
    const val CURRENCY_API_KEY: String = "0430202d52-68ce7481de-sf34mp"

    fun showConfirmationDialog(
        context: Context,
        gif: Int,
        title: String,
        message: String,
        positiveButtonText: String,
        onPositiveClick: () -> Unit,
        negativeButtonText: String = "Cancel",
        onNegativeClick: () -> Unit = {}
    ) {
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.custom_dialog)

        val dialogGif = dialog.findViewById<ImageView>(R.id.dialog_gif)
        val dialogTitle = dialog.findViewById<TextView>(R.id.dialog_title)
        val dialogMessage = dialog.findViewById<TextView>(R.id.dialog_message)
        val positiveButton = dialog.findViewById<Button>(R.id.dialog_button_positive)
        val negativeButton = dialog.findViewById<Button>(R.id.dialog_button_negative)


        Glide.with(context)
            .load(gif)
            .into(dialogGif)
        if (gif == R.drawable.about_us) {
            negativeButton.visibility = View.GONE
        }


        dialogTitle.text = title
        dialogMessage.text = message
        positiveButton.text = positiveButtonText
        negativeButton.text = negativeButtonText

        positiveButton.setOnClickListener {
            onPositiveClick.invoke()
            dialog.dismiss()
        }

        negativeButton.setOnClickListener {
            onNegativeClick.invoke()
            dialog.dismiss()
        }

        dialog.show()
    }


    fun showCouponDetailDialog(
        activity: Activity,
        couponDetail: DiscountCode,
        priceRuleSummary: PriceRuleSummary,
        onDismiss: () -> Unit
    ) {
        val binding = DialogCouponDetailBinding.inflate(activity.layoutInflater)
        val dialog = Dialog(activity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(binding.root)
        val value = -1.0 * priceRuleSummary.value
        binding.tvCouponCode.text = "Code: ${couponDetail.code}"
        binding.tvDiscountValue.text = value.toString() + "%"


        binding.btnCopyCode.setOnClickListener {
            val clipboard = activity.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("Coupon Code", couponDetail.code)
            clipboard.setPrimaryClip(clip)
            Toast.makeText(activity, "Coupon code copied to clipboard", Toast.LENGTH_SHORT).show()
        }

        dialog.setCanceledOnTouchOutside(true)

        dialog.setOnDismissListener {
            onDismiss()
        }

        dialog.show()
    }


    fun playAnimation(view: View, context: Context, animation: Int) {
        view.startAnimation(AnimationUtils.loadAnimation(context, animation))
    }
}


