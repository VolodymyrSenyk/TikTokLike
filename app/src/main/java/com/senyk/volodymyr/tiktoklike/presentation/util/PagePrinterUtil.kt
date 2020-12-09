package com.senyk.volodymyr.tiktoklike.presentation.util

import android.content.Context
import android.os.Build
import android.util.Log
import android.webkit.*
import com.senyk.volodymyr.tiktoklike.data.datasource.HEADER_DEFAULT_USER_AGENT
import javax.inject.Inject

class PagePrinterUtil @Inject constructor(context: Context) {

    private val invisibleWebView = WebView(context)

    private var url = ""

    init {
        invisibleWebView.apply {
            addJavascriptInterface(MyJavaScriptInterface(), "HtmlViewer");
            webChromeClient = object : WebChromeClient() {
                override fun onPermissionRequest(request: PermissionRequest) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        request.grant(request.resources)
                    }
                }
            }
            webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView, url: String) {
                    if (url == this@PagePrinterUtil.url) {
                        view.loadUrl(
                            "javascript:window.HtmlViewer.showHTML" +
                                    "('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>');"
                        )
                    }
                }
            }
        }
        invisibleWebView.settings.apply {
            javaScriptEnabled = true
            userAgentString = HEADER_DEFAULT_USER_AGENT
        }
        WebView.setWebContentsDebuggingEnabled(true)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            CookieManager.getInstance().setAcceptThirdPartyCookies(invisibleWebView, true)
        }
    }

    fun printPageInLogs(url: String) {
        this.url = url
        invisibleWebView.loadUrl(url)
    }

    internal class MyJavaScriptInterface {
        @JavascriptInterface
        fun showHTML(html: String?) {
            if (html.isNullOrEmpty()) return
            val maxLogSize = 1000
            for (i in 0..html.length / maxLogSize) {
                val start = i * maxLogSize
                var end = (i + 1) * maxLogSize
                end = if (end > html.length) html.length else end
                val log = html.substring(start, end)
                Log.i("HTML", log)
            }
        }
    }
}
