package com.skywalker.helper

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.skywalker.R
import java.text.SimpleDateFormat
import java.util.*

object Utils {

    const val contactUsEmail = ""
    const val contactUsSubject = ""
    const val PAGE_PER_ITEM: Int = 50

    //    "http://18.235.75.7:5000/api/v1/"
    val BASE_URL = "https://darkwolve.xyz/api/v1/"
    val HOTSPOT_BASE_URL = "https://nomadhotspotappv1.s3.us-west-1.amazonaws.com/apis/"
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

    fun getHotSpotThemeBackground(theme: String, context: Context): Drawable? {
        return when (theme) {
            "green" -> {
                context.resources.getDrawable(R.drawable.ic_hotspot_bg_one)
            }
            "orange" -> {
                context.resources.getDrawable(R.drawable.ic_hotspot_bg_two)
            }
            "red" -> {
                context.resources.getDrawable(R.drawable.ic_hotspot_bg_three)
            }
            "blue" -> {
                context.resources.getDrawable(R.drawable.ic_hotspot_bg_four)
            }
            else -> {
                context.resources.getDrawable(R.drawable.ic_hotspot_bg_four)
            }
        }
    }

    fun Long.toTimeDateString(): String {
        val dateTime = Date(this)
        val format = SimpleDateFormat("MM/dd/yy HH:mm:ss", Locale.US)
        return format.format(dateTime)
    }

    fun Long.getDateTime(): String? {
        try {
            val sdf = SimpleDateFormat("MM/dd/yy HH:mm:ss")
            val netDate = Date(this * 1000)
            return sdf.format(netDate)
        } catch (e: Exception) {
            return e.toString()
        }
    }
}