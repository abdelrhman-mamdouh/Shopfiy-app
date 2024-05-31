package com.example.exclusive.utilities

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.Window
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AlertDialog
import com.example.exclusive.R
import com.example.exclusive.databinding.GoToLoginDialogBinding

object Constants {
    const val GOOGLE_KEY: String = "AIzaSyCzL-KKINpE6at4j4zH8qNXAB6eMn8Y0L0"
    const val BASE_URL: String = "https://mad44-android-sv-2.myshopify.com/api/2024-04/graphql.json"
    const val CURRENCY_BASE_URL: String = "https://api.fastforex.io/"
    const val API_KEY: String = "43f6205d2d0b257b652e16f5f7663414"
    const val CURRENCY_API_KEY: String = "2a61efd03e-9f8ad1d4f4-se2vx8"

    fun showAlert(context: Context, title: Int, message: String, icon: Int) {
        AlertDialog.Builder(context)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(R.string.app_name) { _, _ -> }
            .setIcon(icon)
            .show()
    }

    fun showDialog(requireActivity: Activity, dialogTitle:String, dialogMessage:String, btnName:String, goToLoginHandle:() -> Unit){
        val inflater = requireActivity.layoutInflater
        val dialog = Dialog(requireActivity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val bind : GoToLoginDialogBinding = GoToLoginDialogBinding.inflate(inflater)
        dialog.setContentView(bind.root)
        dialog.setTitle(dialogTitle)
        bind.warningTitle.text = dialogMessage
        bind.goToLogin.text = btnName
        bind.okBtn.text = requireActivity.getString(R.string.app_name)
        bind.okBtn.setOnClickListener {
            dialog.dismiss()
        }
        bind.goToLogin.setOnClickListener{
            goToLoginHandle()
            dialog.dismiss()
        }
        dialog.setCanceledOnTouchOutside(true)
        dialog.show()
    }

    fun playAnimation(view: View, context: Context, animation: Int) {
        view.startAnimation(AnimationUtils.loadAnimation(context, animation))
    }
}