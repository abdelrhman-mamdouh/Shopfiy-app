package com.example.exclusive.ui.productinfo.view

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.appcompat.app.AlertDialog

class DailogFragment (private val title:String="Are you sure you want to delete it from your watchlist?", private val onDialogPositiveClick:()->Unit, private val onDialogNegativeClick:()->Unit): DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext())
            .setTitle("Warning")
            .setMessage(title)
            .setPositiveButton("OK") { dialog, id ->
                onDialogPositiveClick()
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, id ->

                dialog.dismiss()
            }.create()
    }
}
