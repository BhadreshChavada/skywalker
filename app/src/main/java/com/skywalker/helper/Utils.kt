package com.skywalker.helper

import android.content.Context
import android.view.View
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.skywalker.R

object Utils {

    fun showSnackBar(view: View, message: String, isError: Boolean = false, context: Context) {

        if (isError) {
            Snackbar.make(view, message, Snackbar.LENGTH_LONG)
                .setBackgroundTint(ContextCompat.getColor(context, R.color.red))
                .show()
        } else {
            Snackbar.make(view, message, Snackbar.LENGTH_LONG).show()
        }

    }
}