package com.skywalker.ui.hotspot

import android.content.Context
import android.webkit.JavascriptInterface

open class MyJavaScriptChromeClient(val context: Context,val webViewEvents: WebViewEvents) {

    @JavascriptInterface
    fun purchase_fail() {
        webViewEvents.closeWebview("FAIL")

    }

    @JavascriptInterface
    fun close_webview() {
        webViewEvents.closeWebview("CLOSE")

    }

    @JavascriptInterface
    fun purchase_done(subscription_id :String) {
        webViewEvents.closeWebview("DONE")

    }

    @JavascriptInterface
    fun test_connection() {
        webViewEvents.closeWebview("TEST")

    }

    @JavascriptInterface
    fun test_connection(key:String) {
        webViewEvents.closeWebview("TEST")

    }
}

interface WebViewEvents{
    fun closeWebview(status:String)
}