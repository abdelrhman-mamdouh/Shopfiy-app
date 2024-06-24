package com.example.exclusive.utilities

import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.exclusive.R
import com.google.android.material.snackbar.Snackbar

object SnackbarUtils {

    fun showSnackbar(context: Context, view: View, message: String) {
        val snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT)
        val snackbarView = snackbar.view
        val textView: TextView = snackbarView.findViewById(com.google.android.material.R.id.snackbar_text)
        textView.setTextColor(Color.WHITE)
        snackbarView.setBackgroundColor(ContextCompat.getColor(context, R.color.black))
        snackbar.show()
    }

    fun showTopSnackbar(rootView: View, message: String, color: String) {
        val snackbar = Snackbar.make(rootView, message, Snackbar.LENGTH_SHORT or Snackbar.ANIMATION_MODE_SLIDE)
        val snackbarView = snackbar.view
        val params = snackbarView.layoutParams as FrameLayout.LayoutParams
        params.gravity = Gravity.TOP
        params.topMargin = 80
        snackbarView.layoutParams = params

        val colorRes = when (color) {
            "red" -> android.R.color.holo_red_dark
            else -> android.R.color.holo_green_dark
        }
        snackbarView.setBackgroundColor(ContextCompat.getColor(rootView.context, colorRes))

        snackbar.show()
    }

    fun showSnackbarWithUndo(view: View, message: String, undoListener: View.OnClickListener) {
        val snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG)
            .setAction("Undo", undoListener)

        val snackbarView = snackbar.view
        val textView: TextView = snackbarView.findViewById(com.google.android.material.R.id.snackbar_text)
        textView.setTextColor(Color.WHITE)
        snackbarView.setBackgroundColor(ContextCompat.getColor(view.context, R.color.black))

        val actionTextView: TextView = snackbarView.findViewById(com.google.android.material.R.id.snackbar_action)
        actionTextView.setTextColor(Color.YELLOW)

        snackbar.show()
    }
}
