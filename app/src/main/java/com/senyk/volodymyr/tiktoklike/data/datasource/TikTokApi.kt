package com.senyk.volodymyr.tiktoklike.data.datasource

import com.senyk.volodymyr.tiktoklike.data.datasource.model.response.SetLikeResponse
import com.senyk.volodymyr.tiktoklike.data.datasource.model.response.UserInfoResponse
import com.senyk.volodymyr.tiktoklike.data.datasource.model.response.VideosResponse
import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Url

interface TikTokApi {

    @GET
    fun getQueryCompletable(
        @Header(HEADER_COOKIE) cookie: String,
        @Header(HEADER_CSRF_TOKEN) token: String,
        @Url url: String
    ): Completable

    @POST
    fun postQueryCompletable(
        @Header(HEADER_COOKIE) cookie: String,
        @Header(HEADER_CSRF_TOKEN) token: String,
        @Url url: String
    ): Completable

    @GET
    fun getUser(
        @Header(HEADER_COOKIE) cookie: String,
        @Header(HEADER_CSRF_TOKEN) token: String,
        @Url url: String
    ): Single<UserInfoResponse>

    @POST
    fun setLike(
        @Header(HEADER_COOKIE) cookie: String,
        @Header(HEADER_CSRF_TOKEN) token: String,
        @Url url: String
    ): Single<SetLikeResponse>

    @GET
    fun getVideos(
        @Header(HEADER_COOKIE) cookie: String,
        @Header(HEADER_CSRF_TOKEN) token: String,
        @Url url: String
    ): Single<VideosResponse>
}
