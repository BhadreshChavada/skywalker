package com.skywalker.helper

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.skywalker.R
import com.skywalker.databinding.DialogApiProgressBinding

class ApiProgressDialog (activity: Activity) : Dialog(activity) {
    init {
        setCancelable(true)
    }

    private var binding: DialogApiProgressBinding? = null
    private var mActivity: Activity = activity

    init {
        createDialog()
    }

    private fun createDialog() {
        android.R.style.Theme_Black_NoTitleBar_Fullscreen
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(mActivity),
            R.layout.dialog_api_progress,
            null,
            false
        )
        binding?.let { setContentView(it.root) }
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setProgressGif()
    }

    private fun setProgressGif() {
        binding?.imageView2?.let {
            Glide.with(context).asGif().load(R.raw.anim_progress).diskCacheStrategy(
                DiskCacheStrategy.NONE
            ).skipMemoryCache(true).into(it)
        }
        binding?.imageView2?.let {
            Glide
                .with(context)
                .asGif()
                .load(R.raw.anim_progress)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .listener(object : RequestListener<GifDrawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<GifDrawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        return false
                    }

                    override fun onResourceReady(
                        resource: GifDrawable?,
                        model: Any?,
                        target: Target<GifDrawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        return false
                    }
                }).into(it)
        }
    }
}