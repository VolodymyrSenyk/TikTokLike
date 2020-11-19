package com.senyk.volodymyr.tiktoklike.data.datasource

import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.http.*

interface TikTokApi {

    // Т.к. для получения параметра _signature нужен полностью собранный URL, можно не собирать URL заново,
    // а просто передать его. Поэтому можно создать универсальный метод для всех GET/POST запросов
    // к API TikTok
    @Headers(
        "$HEADER_USER_AGENT: $HEADER_DEFAULT_USER_AGENT",
        "$HEADER_ACCEPT: $HEADER_DEFAULT_ACCEPT",
        "$HEADER_CONTENT_TYPE: $HEADER_DEFAULT_CONTENT_TYPE",
        "$HEADER_DNT: $HEADER_DEFAULT_DNT",
        "$HEADER_ORIGIN: $HEADER_DEFAULT_ORIGIN",
        "$HEADER_REFERER: $HEADER_DEFAULT_REFERER"
    )
    @GET
    fun getQueryCompletable(
        @Header(HEADER_COOKIE) cookie: String,
        @Header(HEADER_CSRF_TOKEN) token: String,
        @Url url: String
    ): Completable

    @Headers(
        "$HEADER_USER_AGENT: $HEADER_DEFAULT_USER_AGENT",
        "$HEADER_ACCEPT: $HEADER_DEFAULT_ACCEPT",
        "$HEADER_CONTENT_TYPE: $HEADER_DEFAULT_CONTENT_TYPE",
        "$HEADER_DNT: $HEADER_DEFAULT_DNT",
        "$HEADER_ORIGIN: $HEADER_DEFAULT_ORIGIN",
        "$HEADER_REFERER: $HEADER_DEFAULT_REFERER"
    )
    @POST
    fun postQueryCompletable(
        @Header(HEADER_COOKIE) cookie: String,
        @Header(HEADER_CSRF_TOKEN) token: String,
        @Url url: String
    ): Completable

    @Headers(
        "$HEADER_USER_AGENT: $HEADER_DEFAULT_USER_AGENT",
        "$HEADER_ACCEPT: $HEADER_DEFAULT_ACCEPT",
        "$HEADER_CONTENT_TYPE: $HEADER_DEFAULT_CONTENT_TYPE",
        "$HEADER_DNT: $HEADER_DEFAULT_DNT",
        "$HEADER_ORIGIN: $HEADER_DEFAULT_ORIGIN",
        "$HEADER_REFERER: $HEADER_DEFAULT_REFERER"
    )
    @GET(ENDPOINT_VIDEOS)
    fun getTrendingStream(
        @Header(HEADER_COOKIE) cookie: String,
        @Header(HEADER_CSRF_TOKEN) csrfToken: String,

        @Query(PARAM_ID) id: String = PARAM_DEFAULT_ID,
        @Query(PARAM_LANGUAGE) language: String = PARAM_DEFAULT_LANGUAGE,
        @Query(PARAM_LANG) lang: String = PARAM_DEFAULT_LANGUAGE,
        @Query(PARAM_USER_ID) userId: String,
        @Query(PARAM_DID) did: String,
        @Query(PARAM_COOKIE_ENABLED_2) cookieEnabled: String = PARAM_DEFAULT_COOKIE_STATUS,
        @Query(PARAM_DEVICE_FINGERPRINT) verifyFp: String = PARAM_DEFAULT_DEVICE_FINGERPRINT,
        @Query(PARAM_SEC_UID) secUid: String = PARAM_DEFAULT_SEC_UID,
        @Query(PARAM_SOURCE_TYPE) sourceType: String = 12.toString(),
        @Query(PARAM_TYPE) type: String = 5.toString(),
        @Query(PARAM_COUNT) count: String = 30.toString(),
        @Query(PARAM_MAX_CURSOR) maxCursor: String = PARAM_DEFAULT_MIN_CURSOR,
        @Query(PARAM_MIN_CURSOR) minCursor: String = PARAM_DEFAULT_MIN_CURSOR,
        @Query(PARAM_SIGNATURE) signature: String
    ): Completable

    @Headers(
        "$HEADER_USER_AGENT: $HEADER_DEFAULT_USER_AGENT",
        "$HEADER_ACCEPT: $HEADER_DEFAULT_ACCEPT",
        "$HEADER_CONTENT_TYPE: $HEADER_DEFAULT_CONTENT_TYPE",
        "$HEADER_DNT: $HEADER_DEFAULT_DNT",
        "$HEADER_ORIGIN: $HEADER_DEFAULT_ORIGIN",
        "$HEADER_REFERER: $HEADER_DEFAULT_REFERER"
    )
    @POST(ENDPOINT_SET_LIKE)
    fun setLike(
        @Header(HEADER_COOKIE) cookie: String,
        @Header(HEADER_CSRF_TOKEN) csrfToken: String,

        @Query(PARAM_APP_ID) appId: String = PARAM_DEFAULT_APP_ID,
        @Query(PARAM_LANGUAGE) language: String = PARAM_DEFAULT_LANGUAGE,
        @Query(PARAM_LANG) lang: String = PARAM_DEFAULT_LANGUAGE,
        @Query(PARAM_AID) aid: String = PARAM_DEFAULT_AID,
        @Query(PARAM_VIDEO_ID) videoId: String,
        @Query(PARAM_USER_ID) userId: String,
        @Query(PARAM_DID) did: String,
        @Query(PARAM_DEVICE_ID) deviceId: String,
        @Query(PARAM_DEVICE_FINGERPRINT) verifyFp: String = PARAM_DEFAULT_DEVICE_FINGERPRINT,
        @Query(PARAM_COOKIE_ENABLED) cookieEnabled: String = PARAM_DEFAULT_COOKIE_STATUS,
        @Query(PARAM_TYPE) type: String,
        @Query(PARAM_SIGNATURE) signature: String
    ): Single<SetLikeResponse>
}
