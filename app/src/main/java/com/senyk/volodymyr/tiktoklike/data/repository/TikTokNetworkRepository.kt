package com.senyk.volodymyr.tiktoklike.data.repository

import android.content.Context
import android.util.Log
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import com.senyk.volodymyr.tiktoklike.data.datasource.*
import com.senyk.volodymyr.tiktoklike.data.datasource.model.response.UserInfoResponse
import com.senyk.volodymyr.tiktoklike.data.datasource.model.response.VideoDetailsResponse
import com.senyk.volodymyr.tiktoklike.data.datasource.model.response.VideosResponse
import com.senyk.volodymyr.tiktoklike.domain.CookieRepository
import com.senyk.volodymyr.tiktoklike.domain.TikTokRepository
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class TikTokNetworkRepository @Inject constructor(
    private val tikTokApi: TikTokApi,
    private val cookieRepository: CookieRepository,
    context: Context
) : TikTokRepository {

    private val webView: WebView = WebView(context)

    private val webViewParser: WebView = WebView(context)

    init {
        webView.settings.apply {
            javaScriptEnabled = true
            userAgentString = HEADER_DEFAULT_USER_AGENT
        }
        webView.loadUrl("")

        webViewParser.settings.apply {
            javaScriptEnabled = true
            userAgentString = HEADER_DEFAULT_USER_AGENT
        }
        webViewParser.addJavascriptInterface(MyJavaScriptInterface(context), "HtmlViewer")
        webViewParser.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {

                /*if (url == "https://www.tiktok.com/@markiv_anastasia?source=h5_m") {
                    webView.loadUrl(
                        "javascript:(function(){" +
                                "button=document.getElementsByTagName('button')[0];" +
                                "console.log('Button 1 is ' + button);" +
                                "})()"
                    )
                    webView.loadUrl(
                        "javascript:(function(){" +
                                "button=document.getElementsByClassName('follow-button')[0];" +
                                "console.log('Button is ' + button);" +
                                "event=document.createEvent('HTMLEvents');" +
                                "event.initEvent('click',true,true);" +
                                "button.dispatchEvent(event);" +
                                "})()"
                    )
                } else {
                    Log.e("OkHttp", url)
                }*/

                webViewParser.loadUrl(
                    "javascript:window.HtmlViewer.showHTML" +
                            "('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>');"
                )
            }
        }
    }

    override fun likeVideo(
        videoId: String,
        userId: String,
        like: Boolean
    ): Completable = cookieRepository.getCookie()
        .flatMapCompletable { cookie ->
            val query = ENDPOINT_SET_LIKE
            val queryOptions = mutableMapOf(
                PARAM_APP_ID to PARAM_DEFAULT_APP_ID,
                PARAM_LANGUAGE to PARAM_DEFAULT_LANGUAGE,
                PARAM_LANG to PARAM_DEFAULT_LANGUAGE,
                PARAM_AID to PARAM_DEFAULT_AID,
                PARAM_VIDEO_ID to videoId,
                PARAM_USER_ID to userId,
                PARAM_DID to getDataFromCookie(cookie = cookie, key = COOKIE_KEY_DEVICE_ID),
                PARAM_DEVICE_ID to getDataFromCookie(cookie = cookie, key = COOKIE_KEY_DEVICE_ID),
                PARAM_DEVICE_FINGERPRINT to PARAM_DEFAULT_DEVICE_FINGERPRINT,
                PARAM_COOKIE_ENABLED_UNDERSCORED to PARAM_DEFAULT_COOKIE_STATUS,
                PARAM_TYPE to like.toInt().toString()
            )
            signUrl(url = getFormattedUrl(endpoint = query, options = queryOptions))
                .observeOn(Schedulers.computation())
                .flatMap { urlSigningCode ->
                    tikTokApi.setLike(
                        cookie = cookie,
                        token = getDataFromCookie(cookie = cookie, key = COOKIE_KEY_CSRF_TOKEN),
                        url = getFormattedUrl(
                            endpoint = query,
                            options = queryOptions.toMutableMap().apply {
                                put(PARAM_SIGNATURE, urlSigningCode)
                            },
                            fullUrl = false
                        )
                    )
                }
                .flatMapCompletable { response ->
                    if (response.isDigg.toBoolean() != like) {
                        Completable.complete()
                    } else {
                        Completable.error(Exception("Operation not successful"))
                    }
                }.observeOn(AndroidSchedulers.mainThread())
        }

    override fun getUserDetails(userId: String): Single<UserInfoResponse> =
        cookieRepository.getCookie()
            .flatMap { cookie ->
                val query = ENDPOINT_USER_DETAILS
                val queryOptions = mutableMapOf(
                    PARAM_APP_ID to PARAM_DEFAULT_APP_ID,
                    PARAM_LANGUAGE to PARAM_DEFAULT_LANGUAGE,
                    PARAM_LANG to PARAM_DEFAULT_LANGUAGE,
                    PARAM_UNIQUE_ID to userId
                )
                signUrl(url = getFormattedUrl(endpoint = query, options = queryOptions))
                    .observeOn(Schedulers.computation())
                    .flatMap { urlSigningCode ->
                        tikTokApi.getUser(
                            cookie = cookie,
                            token = getDataFromCookie(cookie = cookie, key = COOKIE_KEY_CSRF_TOKEN),
                            url = getFormattedUrl(
                                endpoint = query,
                                options = queryOptions.apply {
                                    put(
                                        PARAM_SIGNATURE,
                                        urlSigningCode
                                    )
                                },
                                fullUrl = false
                            )
                        )
                    }
                    .observeOn(AndroidSchedulers.mainThread())
            }

    override fun getTrendingStream(userId: String): Single<VideosResponse> =
        cookieRepository.getCookie()
            .flatMap { cookie ->
                val query = ENDPOINT_TRENDING_VIDEOS
                val queryOptions = mutableMapOf(
                    PARAM_APP_ID to PARAM_DEFAULT_APP_ID,
                    PARAM_LANGUAGE to PARAM_DEFAULT_LANGUAGE,
                    PARAM_LANG to PARAM_DEFAULT_LANGUAGE,
                    PARAM_ID to PARAM_DEFAULT_ID,
                    PARAM_USER_ID to userId,
                    PARAM_DID to getDataFromCookie(cookie = cookie, key = COOKIE_KEY_DEVICE_ID),
                    PARAM_COOKIE_ENABLED to PARAM_DEFAULT_COOKIE_STATUS,
                    PARAM_DEVICE_FINGERPRINT to PARAM_DEFAULT_DEVICE_FINGERPRINT,
                    PARAM_SEC_UID to PARAM_DEFAULT_SEC_UID,
                    PARAM_SOURCE_TYPE to 12.toString(),
                    PARAM_TYPE to 5.toString(),
                    PARAM_COUNT to 30.toString(),
                    PARAM_MAX_CURSOR to PARAM_DEFAULT_MIN_CURSOR,
                    PARAM_MIN_CURSOR to PARAM_DEFAULT_MIN_CURSOR
                )
                signUrl(url = getFormattedUrl(endpoint = query, options = queryOptions))
                    .observeOn(Schedulers.computation())
                    .flatMap { urlSigningCode ->
                        tikTokApi.getVideos(
                            cookie = cookie,
                            token = getDataFromCookie(cookie = cookie, key = COOKIE_KEY_CSRF_TOKEN),
                            url = getFormattedUrl(
                                endpoint = query,
                                options = queryOptions.apply {
                                    put(
                                        PARAM_SIGNATURE,
                                        urlSigningCode
                                    )
                                },
                                fullUrl = false
                            )
                        )
                    }
                    .observeOn(AndroidSchedulers.mainThread())
            }

    override fun getUserVideos(
        userId: String,
        userSecUid: String
    ): Single<VideosResponse> = cookieRepository.getCookie()
        .flatMap { cookie ->
            val query = ENDPOINT_USER_VIDEOS
            val queryOptions = mutableMapOf(
                PARAM_APP_ID to PARAM_DEFAULT_APP_ID,
                PARAM_LANGUAGE to PARAM_DEFAULT_LANGUAGE,
                PARAM_LANG to PARAM_DEFAULT_LANGUAGE,
                PARAM_AID to PARAM_DEFAULT_AID,
                PARAM_ID to userId,
                PARAM_SEC_UID to userSecUid,
                PARAM_COUNT to 30.toString(),
                PARAM_MAX_CURSOR to PARAM_DEFAULT_MIN_CURSOR,
                PARAM_MIN_CURSOR to PARAM_DEFAULT_MIN_CURSOR,
                PARAM_SOURCE_TYPE to 8.toString(),
                PARAM_TYPE to 1.toString(),
            )
            signUrl(url = getFormattedUrl(endpoint = query, options = queryOptions))
                .observeOn(Schedulers.computation())
                .flatMap { urlSigningCode ->
                    tikTokApi.getVideos(
                        cookie = cookie,
                        token = getDataFromCookie(cookie = cookie, key = COOKIE_KEY_CSRF_TOKEN),
                        url = getFormattedUrl(
                            endpoint = query,
                            options = queryOptions.apply {
                                put(
                                    PARAM_SIGNATURE,
                                    urlSigningCode
                                )
                            },
                            fullUrl = false
                        )
                    )
                }
                .observeOn(AndroidSchedulers.mainThread())
        }

    override fun getVideoById(
        videoId: String
    ): Single<VideoDetailsResponse> = cookieRepository.getCookie()
        .flatMap { cookie ->
            val query = ENDPOINT_VIDEO
            val queryOptions = mutableMapOf(
                PARAM_APP_ID to PARAM_DEFAULT_APP_ID,
                PARAM_LANGUAGE to PARAM_DEFAULT_LANGUAGE,
                PARAM_LANG to PARAM_DEFAULT_LANGUAGE,
                PARAM_AID to PARAM_DEFAULT_AID,
                PARAM_ITEM_ID to videoId,
                PARAM_COUNT to 30.toString(),
                PARAM_MAX_CURSOR to PARAM_DEFAULT_MIN_CURSOR,
                PARAM_MIN_CURSOR to PARAM_DEFAULT_MIN_CURSOR,
                PARAM_SOURCE_TYPE to 8.toString(),
                PARAM_TYPE to 1.toString(),
            )
            signUrl(url = getFormattedUrl(endpoint = query, options = queryOptions))
                .observeOn(Schedulers.computation())
                .flatMap { urlSigningCode ->
                    tikTokApi.getVideoDetails(
                        cookie = cookie,
                        token = getDataFromCookie(cookie = cookie, key = COOKIE_KEY_CSRF_TOKEN),
                        url = getFormattedUrl(
                            endpoint = query,
                            options = queryOptions.apply {
                                put(
                                    PARAM_SIGNATURE,
                                    urlSigningCode
                                )
                            },
                            fullUrl = false
                        )
                    )
                }
                .observeOn(AndroidSchedulers.mainThread())
        }

    // gets _signature parameter, that needs almost all requests
    // requires full url of API call, performs calculations via obfuscated JS code
    private fun signUrl(url: String): Single<String> =
        Single.create { emitter ->
            val jsFunction = webView.context.assets.open("acrawler.js")
                .bufferedReader().use { reader -> reader.readText() }
            val getUrlSigningCodeScript = "$jsFunction; byted_acrawler.sign({ url: \"$url\" })"
            webView.evaluateJavascript(getUrlSigningCodeScript) { result ->
                emitter.onSuccess(result.replace("\"", ""))
            }
        }

    private fun getFormattedUrl(
        endpoint: String,
        options: Map<String, String>,
        fullUrl: Boolean = true
    ): String {
        val url = StringBuilder()
        if (fullUrl) {
            url.append(BASE_URL)
        }
        url.append(endpoint)
        url.append(getOptionsUrl(options))
        return url.toString()
    }

    private fun getOptionsUrl(options: Map<String, String>): String {
        val optionsUrl = StringBuilder()
        if (options.isNotEmpty()) {
            optionsUrl.append("?")
        }
        options.keys.forEach { key ->
            if (optionsUrl.isNotEmpty() && optionsUrl.last() != '?') {
                optionsUrl.append("&")
            }
            optionsUrl.append("$key=${options[key]}")
        }
        return optionsUrl.toString()
    }

    private fun getDataFromCookie(cookie: String, key: String): String = cookie
        .substringAfterLast(key)
        .split("; ")[0]
        .replace("=", "")

    private fun Boolean.toInt(): Int = if (this) 1 else 0

    private fun Int.toBoolean(): Boolean = this != 0

    override fun followUser(
        userId: String,
        userToFollowId: String,
        follow: Boolean
    ): Completable {
        webViewParser.loadUrl("https://www.tiktok.com/@markiv_anastasia?source=h5_m")

        return Completable.complete()
    } /*cookieRepository.getCookie()
        .flatMapCompletable { cookie ->
            val query = ENDPOINT_FOLLOW_USER
            val queryOptions = mutableMapOf(
                "aid" to 1988.toString(),
                "app_name" to "tiktok_web",
                "device_platform" to "web_pc",
                "referer" to "",
                "root_referer" to "https:%2F%2Fwww.google.com%2F",
                "user_agent" to "Mozilla%2F5.0+(Macintosh%3B+Intel+Mac+OS+X+10_15_7)+AppleWebKit%2F537.36+(KHTML,+like+Gecko)+Chrome%2F86.0.4240.198+Safari%2F537.36",
                "cookie_enabled" to true.toString(),
                "screen_width" to 1920.toString(),
                "screen_height" to 1080.toString(),
                "browser_language" to "ru-RU",
                "browser_platform" to "MacIntel",
                "browser_name" to "Mozilla",
                "browser_version" to "5.0+(Macintosh%3B+Intel+Mac+OS+X+10_15_7)+AppleWebKit%2F537.36+(KHTML,+like+Gecko)+Chrome%2F86.0.4240.198+Safari%2F537.36",
                "browser_online" to true.toString(),
                "ac" to "4g",
                "timezone_name" to "Europe%2FKiev",
                "page_referer" to "https:%2F%2Fwww.tiktok.com%2F",
                "priority_region" to "",
                "verifyFp" to "verify_khlv3g19_MFc8LiTO_QXfi_48KB_9g1G_5f8bq9kBPx5m",
                "appId" to 1233.toString(),
                "region" to "UA",
                "appType" to "m",
                "isAndroid" to false.toString(),
                "isMobile" to false.toString(),
                "isIOS" to false.toString(),
                "OS" to "mac",
                "did" to "6896044981934900742",
                "device_id" to "6896044981934900742",
                "type" to 1.toString(),
                "user_id" to "6889113253596152834",
                "from" to 19.toString(),
                "channel_id" to 3.toString(),
                "from_pre" to 0.toString(),
                "app_language" to "ru",
                "current_region" to "UA",
                "fromWeb" to 1.toString(),
            )
            signUrl(url = getFormattedUrl(endpoint = query, options = queryOptions))
                .observeOn(Schedulers.computation())
                .map { urlSigningCode ->
                    getFormattedUrl(
                        endpoint = query,
                        options = queryOptions.apply {
                            put(PARAM_SIGNATURE, "_02B4Z6wo00901e2PLDQAAICCbGHgTgEp7G3tjiiAACSt6d")
                        },
                        fullUrl = false
                    )
                }
                .flatMapCompletable { url ->
                    tikTokApi.followUserPost(
                        //   cookie = cookie,
                        //   token = getDataFromCookie(cookie = cookie, key = COOKIE_KEY_CSRF_TOKEN),
                        url = url
                    )
                }.observeOn(AndroidSchedulers.mainThread())
        }*/

    inner class MyJavaScriptInterface(private val context: Context) {
        @JavascriptInterface
        fun showHTML(html: String?) {
            if (html == null) return
            val maxLogSize = 1000
            for (i in 0..html.length / maxLogSize) {
                val start = i * maxLogSize
                var end = (i + 1) * maxLogSize
                end = if (end > html.length) html.length else end
                val log = html.substring(start, end)
                if (log.contains("follow-button jsx-3251180706 jsx-683523640 share-follow tiktok-btn-pc tiktok-btn-pc-medium")) {
                    this@TikTokNetworkRepository.webViewParser.evaluateJavascript(
                        "javascript:(function(){" +
                                "button=document.getElementsByClassName('follow-button')[0];" +
                                "console.log('Button is ' + button);" +
                                "event=document.createEvent('HTMLEvents');" +
                                "event.initEvent('click',true,true);" +
                                "button.dispatchEvent(event);" +
                                "})()"
                    ) {
                        Log.e("OkHttp", it)
                    }
                }
            }
        }
        /* @JavascriptInterface
         fun showHTML(html: String?) {
             if (html == null) return
             val maxLogSize = 1000
             for (i in 0..html.length / maxLogSize) {
                 val start = i * maxLogSize
                 var end = (i + 1) * maxLogSize
                 end = if (end > html.length) html.length else end
                 Log.v("OkHttp", html.substring(start, end))
             }
         }*/
    }
}
