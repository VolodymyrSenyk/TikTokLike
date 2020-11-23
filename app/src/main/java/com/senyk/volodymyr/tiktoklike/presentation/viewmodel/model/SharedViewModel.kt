package com.senyk.volodymyr.tiktoklike.presentation.viewmodel.model

import android.util.Log
import com.senyk.volodymyr.tiktoklike.domain.CookieRepository
import com.senyk.volodymyr.tiktoklike.domain.TikTokRepository
import com.senyk.volodymyr.tiktoklike.presentation.viewmodel.base.BaseRxViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class SharedViewModel @Inject constructor(
    private val cookieRepository: CookieRepository,
    private val tikTokRepository: TikTokRepository
) : BaseRxViewModel() {

    fun onCookieGotClick(cookie: String) {
        cookieRepository.setCookie(cookie)
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(this::onError) { onComplete("Cookie saved") }
            .apply(this::addDisposable)
    }

    fun onTestClick() {
        onChainTestClick()
    }

    fun onChainTestClick() {
        val currentUserLogin = "volodymyrsenyk0"
        val targetUserLogin = "markiv_anastasia"

        var currentUserId = ""
        var targetUserId = ""
        var targetUserSecUid = ""
        var targetVideoId = ""
        tikTokRepository.getUserDetails(userId = currentUserLogin)
            .flatMap { user ->
                currentUserId = user.userInfo?.user?.id ?: ""
                Log.d(tag, "currentUserId = $currentUserId")
                tikTokRepository.getTrendingStream(userId = currentUserId)
            }
            .flatMapCompletable { videos ->
                targetVideoId = videos.items?.get(0)?.id ?: ""
                Log.d(tag, "targetVideoId = $targetVideoId")
                tikTokRepository.likeVideo(
                    userId = currentUserId,
                    videoId = targetVideoId,
                    like = true
                )
            }
            .andThen(tikTokRepository.getUserDetails(userId = targetUserLogin))
            .flatMap { targetUser ->
                targetUserId = targetUser.userInfo?.user?.id ?: ""
                targetUserSecUid = targetUser.userInfo?.user?.secUid ?: ""
                Log.d(tag, "targetUserId = $targetUserId")
                Log.d(tag, "targetUserSecUid = $targetUserSecUid")
                tikTokRepository.getUserVideos(
                    userId = targetUserId,
                    userSecUid = targetUserSecUid
                )
            }
            .flatMapCompletable { videos ->
                targetVideoId = videos.items?.get(0)?.id ?: ""
                Log.d(tag, "targetVideoId = $targetVideoId")
                tikTokRepository.likeVideo(
                    userId = currentUserId,
                    videoId = targetVideoId,
                    like = true
                )
            }
            .andThen {
                Log.d(tag, "currentUserId = $currentUserId")
                Log.d(tag, "targetUserId = $targetUserId")
                tikTokRepository.followUser(
                    userId = currentUserId,
                    userToFollowId = targetUserId,
                    true
                )
            }
            .subscribeBy(this::onError) { onComplete("Test completed") }
            .apply(this::addDisposable)
    }

    fun onGetUserDetailsClick() {
        tikTokRepository.getUserDetails(userId = TEST_USER_LOGIN)
            .subscribeBy(this::onError) { onComplete("GetUserDetails completed") }
            .apply(this::addDisposable)
    }

    fun onGetVideosClick() {
        tikTokRepository.getTrendingStream(userId = TEST_USER_ID)
            .subscribeBy(this::onError) { onComplete("GetVideos completed") }
            .apply(this::addDisposable)
    }

    fun onGetUserVideosClick() {
        tikTokRepository.getUserVideos(
            userId = TEST_USER_TO_FOLLOW_ID,
            userSecUid = TEST_USER_TO_FOLLOW_SEC_UID
        )
            .subscribeBy(this::onError) { onComplete("FollowUser completed") }
            .apply(this::addDisposable)
    }

    fun onSetLikeClick() {
        tikTokRepository.likeVideo(
            userId = TEST_USER_ID,
            videoId = TEST_VIDEO_TO_LIKE_ID,
            like = true
        )
            .subscribeBy(this::onError) { onComplete("SetLike completed") }
            .apply(this::addDisposable)
    }

    fun onRemoveLikeClick() {
        tikTokRepository.likeVideo(
            videoId = TEST_VIDEO_TO_LIKE_ID,
            userId = TEST_USER_ID,
            like = false
        )
            .subscribeBy(this::onError) { onComplete("RemoveLike completed") }
            .apply(this::addDisposable)
    }

    fun onFollowUserClick() {
        tikTokRepository.followUser(
            userId = TEST_USER_ID,
            userToFollowId = TEST_USER_TO_FOLLOW_ID,
            follow = true
        )
            .subscribeBy(this::onError) { onComplete("FollowUser completed") }
            .apply(this::addDisposable)
    }

    companion object {
        private const val TEST_USER_LOGIN = "volodymyrsenyk0"
        private const val TEST_USER_ID = "6892969089498612741"

        private const val TEST_VIDEO_TO_LIKE_ID = "6893087314021485825"

        private const val TEST_USER_TO_FOLLOW_LOGIN = "markiv_anastasia"
        private const val TEST_USER_TO_FOLLOW_ID = "6661611121231036421"
        private const val TEST_USER_TO_FOLLOW_SEC_UID =
            "MS4wLjABAAAA1veG3rs2dMlkbW__TSFdq_cpaxVmACXaYKuOeKj4MB8qIPcivHD5Ha5_p03TgBU2"
    }
}
