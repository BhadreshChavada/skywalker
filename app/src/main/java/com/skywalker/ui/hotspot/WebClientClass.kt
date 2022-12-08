package game.number.webviewdemo

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.graphics.Bitmap
import android.webkit.WebView
import android.webkit.WebViewClient
import com.google.android.a.c
import com.skywalker.databinding.FragmentHotspotHomeBinding
import com.skywalker.helper.ApiProgressDialog


class WebClientClass(val context: Context) : WebViewClient() {

    private lateinit var mProgressDialog: ApiProgressDialog

    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
        super.onPageStarted(view, url, favicon)
        mProgressDialog = ApiProgressDialog(context as Activity)
        mProgressDialog.show()
    }

    override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, url)
        mProgressDialog.dismiss()
    }
}