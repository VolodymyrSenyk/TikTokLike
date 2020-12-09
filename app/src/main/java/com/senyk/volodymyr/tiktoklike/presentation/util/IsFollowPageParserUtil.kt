package com.senyk.volodymyr.tiktoklike.presentation.util

import android.content.Context
import android.os.Build
import android.webkit.*
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.senyk.volodymyr.tiktoklike.data.datasource.HEADER_DEFAULT_USER_AGENT
import javax.inject.Inject

class IsFollowPageParserUtil @Inject constructor(context: Context) {

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
                    if (url == this@IsFollowPageParserUtil.url) {
                        view.loadUrl(
                            "javascript:window.HtmlViewer.checkFollowButtonStyle" +
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

    private val _isAlreadyFollowed = MutableLiveData<Boolean>()
    val isAlreadyFollowed: LiveData<Boolean>
        get() = _isAlreadyFollowed

    fun check(userName: String) {
        url = "https://www.tiktok.com/@$userName?source=h5_m"
        invisibleWebView.loadUrl(url)
    }

    inner class MyJavaScriptInterface {
        @JavascriptInterface
        fun checkFollowButtonStyle(html: String?) {
            _isAlreadyFollowed.postValue(
                html?.contains("button type=\"button\" class=\"follow-button jsx-3251180706 jsx-683523640 share-follow tiktok-btn-pc tiktok-btn-pc-medium tiktok-btn-pc-primary\"") == false
            )
        }
    }
}
