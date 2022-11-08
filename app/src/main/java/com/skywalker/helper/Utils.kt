package com.skywalker.helper

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.skywalker.R

object Utils {

//    "https://darkwolve.xyz/api/v1/"
    val BASE_URL = "http://18.235.75.7:5000/api/v1/"
    val STRIPE_PUBLIC_KEY =
        "pk_test_RIcFVHQ06soRIuzMtrlqYeiy008HScBaHU"

    fun showSnackBar(view: View, message: String, isError: Boolean = false, context: Context) {

        if (isError) {
            Snackbar.make(view, message, Snackbar.LENGTH_LONG)
                .setBackgroundTint(ContextCompat.getColor(context, R.color.red))
                .show()
        } else {
            Snackbar.make(view, message, Snackbar.LENGTH_LONG).show()
        }

    }

    fun getCardThemeBackground(theme: Int, context: Context): Drawable? {
        return when (theme) {
            0 -> {
                context.resources.getDrawable(R.drawable.bg_card_1)
            }
            1 -> {
                context.resources.getDrawable(R.drawable.bg_card_2)
            }
            else -> {
                context.resources.getDrawable(R.drawable.bg_card_3)
            }
        }
    }

    fun getCardThemeImage(theme: Int, context: Context): Drawable? {
        return when (theme) {
            0 -> {
                context.resources.getDrawable(R.drawable.iv_card_1)
            }
            1 -> {
                context.resources.getDrawable(R.drawable.iv_card_2)
            }
            else -> {
                context.resources.getDrawable(R.drawable.iv_card_3)
            }
        }
    }
}