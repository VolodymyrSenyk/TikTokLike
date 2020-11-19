package com.senyk.volodymyr.tiktoklike.data.repository

import android.content.Context
import android.webkit.WebView
import com.senyk.volodymyr.tiktoklike.data.datasource.*
import com.senyk.volodymyr.tiktoklike.domain.TikTokRepository
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class TikTokNetworkRepository @Inject constructor(
    private val tikTokApi: TikTokApi,
    context: Context
) : TikTokRepository {

    private val webView: WebView = WebView(context)

    init {
        webView.settings.apply {
            javaScriptEnabled = true
            userAgentString = HEADER_DEFAULT_USER_AGENT
        }
        // load empty url for correct signUrl method invocation
        // without this string webView.evaluateJavascript will return incorrect code
        webView.loadUrl("")
    }

    // setLike method that uses universal POST request, so returns Completable
    /*override fun setLike(
        cookie: String,
        videoId: String,
        userId: String,
        like: Boolean
    ): Completable {
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
            PARAM_COOKIE_ENABLED to PARAM_DEFAULT_COOKIE_STATUS,
            PARAM_TYPE to (if (like) 1 else 0).toString()
        )
        return signUrl(url = getFormattedUrl(endpoint = query, options = queryOptions))
            .observeOn(Schedulers.computation())
            .flatMapCompletable { urlSigningCode ->
                tikTokApi.queryPostGetCompletable(
                    cookie = cookie,
                    token = getDataFromCookie(cookie = cookie, key = COOKIE_KEY_CSRF_TOKEN),
                    url = getFormattedUrl(
                        endpoint = query,
                        options = queryOptions.apply { put(PARAM_SIGNATURE, urlSigningCode) },
                        fullUrl = false
                    )
                )
            }.observeOn(AndroidSchedulers.mainThread())
    }*/

    override fun setLike(
        cookie: String,
        videoId: String,
        userId: String,
        like: Boolean
    ): Completable {
        // get API call url
        val query = ENDPOINT_SET_LIKE
        // build API call parameters map
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
            PARAM_COOKIE_ENABLED to PARAM_DEFAULT_COOKIE_STATUS,
            PARAM_TYPE to like.toInt().toString()
        )
        return signUrl(url = getFormattedUrl(endpoint = query, options = queryOptions))
            .observeOn(Schedulers.computation())
            .flatMap { urlSigningCode ->
                tikTokApi.setLike(
                    cookie = cookie,
                    csrfToken = getDataFromCookie(
                        cookie = cookie,
                        key = COOKIE_KEY_CSRF_TOKEN
                    ),
                    appId = queryOptions[PARAM_APP_ID] ?: PARAM_DEFAULT_APP_ID,
                    language = queryOptions[PARAM_LANGUAGE] ?: PARAM_DEFAULT_LANGUAGE,
                    lang = queryOptions[PARAM_LANG] ?: PARAM_DEFAULT_LANGUAGE,
                    aid = queryOptions[PARAM_AID] ?: PARAM_DEFAULT_AID,
                    videoId = queryOptions[PARAM_VIDEO_ID] ?: videoId,
                    userId = queryOptions[PARAM_USER_ID] ?: userId,
                    did = queryOptions[PARAM_DID] ?: getDataFromCookie(
                        cookie = cookie,
                        key = COOKIE_KEY_DEVICE_ID
                    ),
                    deviceId = queryOptions[PARAM_DEVICE_ID] ?: getDataFromCookie(
                        cookie = cookie,
                        key = COOKIE_KEY_DEVICE_ID
                    ),
                    verifyFp = queryOptions[PARAM_DEVICE_FINGERPRINT]
                        ?: PARAM_DEFAULT_DEVICE_FINGERPRINT,
                    cookieEnabled = queryOptions[PARAM_COOKIE_ENABLED]
                        ?: PARAM_DEFAULT_COOKIE_STATUS,
                    type = queryOptions[PARAM_TYPE] ?: like.toInt().toString(),
                    signature = urlSigningCode
                )
            }
            .flatMapCompletable { response ->
                // API returns isDigg = 0 if video is liked and isDigg = 1 if video is without like
                if (response.isDigg.toBoolean() != like) {
                    Completable.complete()
                } else {
                    Completable.error(Exception("Operation not successful"))
                }
            }.observeOn(AndroidSchedulers.mainThread())
    }

    override fun getUserDetails(cookie: String, userId: String): Completable {
        val query = ENDPOINT_USER_DETAILS
        val queryOptions = mutableMapOf(
            PARAM_APP_ID to PARAM_DEFAULT_APP_ID,
            PARAM_LANGUAGE to PARAM_DEFAULT_LANGUAGE,
            PARAM_LANG to PARAM_DEFAULT_LANGUAGE,
            PARAM_UNIQUE_ID to userId
        )
        return signUrl(url = getFormattedUrl(endpoint = query, options = queryOptions))
            .observeOn(Schedulers.computation())
            .flatMapCompletable { urlSigningCode ->
                tikTokApi.getQueryCompletable(
                    cookie = cookie,
                    token = getDataFromCookie(cookie = cookie, key = COOKIE_KEY_CSRF_TOKEN),
                    url = getFormattedUrl(
                        endpoint = query,
                        options = queryOptions.apply { put(PARAM_SIGNATURE, urlSigningCode) },
                        fullUrl = false
                    )
                )
            }.observeOn(AndroidSchedulers.mainThread())
    }

    override fun getTrendingStream(cookie: String, userId: String): Completable {
        val query = ENDPOINT_VIDEOS
        val queryOptions = mutableMapOf(
            PARAM_APP_ID to PARAM_DEFAULT_APP_ID,
            PARAM_LANGUAGE to PARAM_DEFAULT_LANGUAGE,
            PARAM_LANG to PARAM_DEFAULT_LANGUAGE,
            PARAM_ID to PARAM_DEFAULT_ID,
            PARAM_USER_ID to userId,
            PARAM_DID to getDataFromCookie(cookie = cookie, key = COOKIE_KEY_DEVICE_ID),
            PARAM_COOKIE_ENABLED_2 to PARAM_DEFAULT_COOKIE_STATUS,
            PARAM_DEVICE_FINGERPRINT to PARAM_DEFAULT_DEVICE_FINGERPRINT,
            PARAM_SEC_UID to PARAM_DEFAULT_SEC_UID,
            PARAM_SOURCE_TYPE to 12.toString(),
            PARAM_TYPE to 5.toString(),
            PARAM_COUNT to 30.toString(),
            PARAM_MAX_CURSOR to PARAM_DEFAULT_MIN_CURSOR,
            PARAM_MIN_CURSOR to PARAM_DEFAULT_MIN_CURSOR
        )
        return signUrl(url = getFormattedUrl(endpoint = query, options = queryOptions))
            .observeOn(Schedulers.computation())
            .flatMapCompletable { urlSigningCode ->
                tikTokApi.getQueryCompletable(
                    cookie = cookie,
                    token = getDataFromCookie(cookie = cookie, key = COOKIE_KEY_CSRF_TOKEN),
                    url = getFormattedUrl(
                        endpoint = query,
                        options = queryOptions.apply { put(PARAM_SIGNATURE, urlSigningCode) },
                        fullUrl = false
                    )
                )
            }.observeOn(AndroidSchedulers.mainThread())
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
}
