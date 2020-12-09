package com.senyk.volodymyr.tiktoklike.presentation.util

import android.content.Context
import android.os.Build
import android.util.Log
import android.webkit.*
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.senyk.volodymyr.tiktoklike.data.datasource.BASE_URL
import com.senyk.volodymyr.tiktoklike.data.datasource.ENDPOINT_FOLLOW_USER
import com.senyk.volodymyr.tiktoklike.data.datasource.HEADER_DEFAULT_USER_AGENT
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class GetHeadersForFollowJsClickUtil @Inject constructor(
    context: Context
) {

    private val tag = this.javaClass.simpleName

    private val invisibleWebView = WebView(context)

    private var url = ""

    private val _headers = MutableLiveData<Map<String, String>>()
    val headers: LiveData<Map<String, String>>
        get() = _headers

    init {
        invisibleWebView.apply {
            webChromeClient = object : WebChromeClient() {
                override fun onPermissionRequest(request: PermissionRequest) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        request.grant(request.resources)
                    }
                }
            }
            webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView, url: String) {
                    if (url == this@GetHeadersForFollowJsClickUtil.url) performJsClick()
                }

                override fun shouldInterceptRequest(
                    view: WebView?,
                    request: WebResourceRequest?
                ): WebResourceResponse? {
                    val url = request?.url.toString()
                    val headers = request?.requestHeaders
                    if (headers.isNullOrEmpty()) return super.shouldInterceptRequest(view, request)
                    if (url.startsWith(BASE_URL + ENDPOINT_FOLLOW_USER)) {
                        _headers.value = mapOf(
                            A to (headers[A] ?: ""),
                            B to (headers[B] ?: ""),
                            C to (headers[C] ?: ""),
                            D to (headers[D] ?: ""),
                            F to (headers[F] ?: "")
                        )
                    }
                    return super.shouldInterceptRequest(view, request)
                }
            }
        }
        invisibleWebView.settings.apply {
            javaScriptEnabled = true
            useWideViewPort = true
            loadWithOverviewMode = true
            builtInZoomControls = true
            domStorageEnabled = true
            displayZoomControls = false
            userAgentString = HEADER_DEFAULT_USER_AGENT
        }
        WebView.setWebContentsDebuggingEnabled(true)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            CookieManager.getInstance().setAcceptThirdPartyCookies(invisibleWebView, true)
        }
    }

    fun followUser(userName: String) {
        url = "https://www.tiktok.com/@$userName?source=h5_m"
        invisibleWebView.loadUrl(url)
    }

    private fun performJsClick() {
        invisibleWebView.evaluateJavascript(
            """javascript:(function(){
                    var button = document.getElementsByClassName('follow-button jsx-3251180706 jsx-683523640 share-follow tiktok-btn-pc tiktok-btn-pc-medium tiktok-btn-pc-primary')[0];
                    if (typeof button === 'undefined') {
                        console.log('Button is ' + button + '. Exit script');
                        return;
                    }
                    var backgroundColor = getComputedStyle(button).getPropertyValue('background-color');
                    console.log('Button background color is ' + backgroundColor);
                    var text = button.firstChild.nodeValue;
                    console.log('Text ' + text);
                    if (text === 'Follow') {
                        var event = document.createEvent('HTMLEvents');
                        event.initEvent('click',true,true);
                        button.dispatchEvent(event);
                        console.log('Click on follow button');
                    }
                    })()"""
        ) {
            Log.i(tag, "Successfully clicked")
        }
    }

    companion object {
        const val A = "hTc6j8Njvn-a"
        const val B = "hTc6j8Njvn-b"
        const val C = "hTc6j8Njvn-c"
        const val D = "hTc6j8Njvn-d"
        const val F = "hTc6j8Njvn-f"
    }
}
