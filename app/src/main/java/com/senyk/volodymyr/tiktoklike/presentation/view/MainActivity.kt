package com.senyk.volodymyr.tiktoklike.presentation.view

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.webkit.*
import android.widget.Toast
import androidx.activity.viewModels
import com.senyk.volodymyr.tiktoklike.R
import com.senyk.volodymyr.tiktoklike.data.datasource.BASE_URL
import com.senyk.volodymyr.tiktoklike.data.datasource.HEADER_DEFAULT_USER_AGENT
import com.senyk.volodymyr.tiktoklike.presentation.view.base.BaseActivity
import com.senyk.volodymyr.tiktoklike.presentation.viewmodel.model.SharedViewModel
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : BaseActivity() {

    override val layoutRes = R.layout.activity_main

    val viewModel by viewModels<SharedViewModel>(factoryProducer = { viewModelFactory })

    override fun onResume() {
        super.onResume()
        setUpWebViewDefaults(webView)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.toastMessage.observe(this, { message ->
            if (!message.isNullOrEmpty()) {
                Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(menuItem: MenuItem): Boolean = when (menuItem.itemId) {
        R.id.menuActionCookieGot -> {
            viewModel.onCookieGotClick(CookieManager.getInstance().getCookie(BASE_URL)); true
        }
        R.id.menuActionTestRequest -> {
            viewModel.onTestClick(); true
        }
        else -> super.onOptionsItemSelected(menuItem)
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setUpWebViewDefaults(webView: WebView) {
        webView.apply {
            webChromeClient = object : WebChromeClient() {
                override fun onPermissionRequest(request: PermissionRequest) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        request.grant(request.resources)
                    }
                }
            }
            webViewClient = object : WebViewClient() {}
            loadUrl(BASE_URL)
        }
        webView.settings.apply {
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
            CookieManager.getInstance().setAcceptThirdPartyCookies(webView, true)
        }
    }

    override fun onBackPressed() {
        if (webView.canGoBack()) webView.goBack() else super.onBackPressed()
    }
}
