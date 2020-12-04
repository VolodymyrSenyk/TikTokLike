package com.senyk.volodymyr.tiktoklike.domain

import androidx.annotation.CheckResult
import com.senyk.volodymyr.tiktoklike.data.datasource.model.response.UserInfoResponse
import com.senyk.volodymyr.tiktoklike.data.datasource.model.response.VideoDetailsResponse
import com.senyk.volodymyr.tiktoklike.data.datasource.model.response.VideosResponse
import io.reactivex.Completable
import io.reactivex.Single

interface TikTokRepository {

    @CheckResult
    fun getUserDetails(userId: String): Single<UserInfoResponse>

    @CheckResult
    fun getTrendingStream(userId: String): Single<VideosResponse>

    @CheckResult
    fun getUserVideos(userId: String, userSecUid: String): Single<VideosResponse>

    @CheckResult
    fun getVideoById(videoId: String): Single<VideoDetailsResponse>

    @CheckResult
    fun likeVideo(videoId: String, userId: String, like: Boolean): Completable

    @CheckResult
    fun followUser(userId: String, userToFollowId: String, follow: Boolean): Completable
}
