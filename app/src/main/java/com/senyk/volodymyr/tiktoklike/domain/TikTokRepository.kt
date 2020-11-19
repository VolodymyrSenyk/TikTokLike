package com.senyk.volodymyr.tiktoklike.domain

import androidx.annotation.CheckResult
import io.reactivex.Completable

interface TikTokRepository {

    @CheckResult
    fun setLike(
        cookie: String,
        videoId: String,
        userId: String,
        like: Boolean
    ): Completable

    @CheckResult
    fun getUserDetails(cookie: String, userId: String): Completable

    @CheckResult
    fun getTrendingStream(cookie: String, userId: String): Completable
}
