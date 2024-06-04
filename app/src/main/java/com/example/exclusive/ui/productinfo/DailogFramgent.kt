package com.example.exclusive.ui.productinfo

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.appcompat.app.AlertDialog

class DailogFramgent (private val onDialogPositiveClick:()->Unit,private val onDialogNegativeClick:()->Unit): DialogFragment() {



    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext())
            .setTitle("Warning")
            .setMessage("Are you sure you want to delete it from your watchlist?")
            .setPositiveButton("OK") { dialog, id ->
                onDialogPositiveClick()
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, id ->
                onDialogNegativeClick()
                dialog.dismiss()
            }
            .create()
    }
}
