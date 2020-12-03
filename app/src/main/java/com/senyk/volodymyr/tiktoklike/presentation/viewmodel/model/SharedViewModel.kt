package com.senyk.volodymyr.tiktoklike.presentation.viewmodel.model

import android.util.Log
import com.senyk.volodymyr.tiktoklike.data.datasource.model.inner.Items
import com.senyk.volodymyr.tiktoklike.domain.CookieRepository
import com.senyk.volodymyr.tiktoklike.domain.TikTokRepository
import com.senyk.volodymyr.tiktoklike.presentation.viewmodel.base.BaseRxViewModel
import io.reactivex.Observable
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
        onFollowUserSimple()
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
        tikTokRepository.getUserDetails(userId = TEST_CURRENT_USER_LOGIN)
            .subscribeBy(this::onError) { onComplete("GetUserDetails completed") }
            .apply(this::addDisposable)
    }

    fun onGetVideosClick() {
        tikTokRepository.getUserDetails(userId = TEST_CURRENT_USER_LOGIN)
            .flatMap { user ->
                tikTokRepository.getTrendingStream(userId = user.userInfo?.user?.id ?: "")
            }
            .subscribeBy(this::onError) { onComplete("GetVideos completed") }
            .apply(this::addDisposable)
    }

    fun onGetUserVideosClick() {
        tikTokRepository.getUserDetails(userId = TEST_TARGET_USER_LOGIN)
            .flatMap { user ->
                tikTokRepository.getUserVideos(
                    userId = user.userInfo?.user?.id ?: "",
                    userSecUid = user.userInfo?.user?.secUid ?: ""
                )
            }
            .subscribeBy(this::onError) { onComplete("GetUserVideos completed") }
            .apply(this::addDisposable)
    }

    fun onSetLikeClick() {
        tikTokRepository.getUserDetails(userId = TEST_CURRENT_USER_LOGIN)
            .flatMapCompletable { user ->
                tikTokRepository.likeVideo(
                    userId = user.userInfo?.user?.id ?: "",
                    videoId = "6893087314021485825",
                    like = true
                )
            }
            .subscribeBy(this::onError) { onComplete("SetLike completed") }
            .apply(this::addDisposable)
    }

    fun onFollowUserClick() {
        tikTokRepository.getUserDetails(userId = TEST_CURRENT_USER_LOGIN)
            .zipWith(tikTokRepository.getUserDetails(userId = TEST_TARGET_USER_LOGIN)) { currentUser, targetUser ->
                mapOf(
                    CURRENT_USER_KEY to currentUser.userInfo,
                    TARGET_USER_KEY to targetUser.userInfo
                )
            }
            .flatMapCompletable { usersList ->
                tikTokRepository.followUser(
                    userId = usersList[CURRENT_USER_KEY]?.user?.id ?: "",
                    userToFollowId = usersList[TARGET_USER_KEY]?.user?.id ?: "",
                    follow = true
                )
            }
            .subscribeBy(this::onError) { onComplete("FollowUser completed") }
            .apply(this::addDisposable)
    }

    fun onFollowUserSimple() {
        tikTokRepository.followUser(
            userId = "6892969089498612741",
            userToFollowId = "6661611121231036421",
            follow = true
        )
            .subscribeBy(this::onError) { onComplete("FollowUser completed") }
            .apply(this::addDisposable)
    }

    fun onFollowAllUsersFromTrendClick() {
        var currentUserId = ""
        tikTokRepository.getUserDetails(userId = TEST_CURRENT_USER_LOGIN)
            .flatMap { user ->
                currentUserId = user.userInfo?.user?.id ?: ""
                tikTokRepository.getTrendingStream(userId = currentUserId)
            }
            .flatMapObservable { videos ->
                Observable.create<Items> { emitter ->
                    videos.items?.forEach { video ->
                        video?.let { emitter.onNext(video) }
                    }
                    emitter.onComplete()
                }
            }
            .flatMapSingle { video ->
                tikTokRepository.getUserDetails(
                    userId = video.author?.uniqueId ?: ""
                )
            }
            .flatMapCompletable { user ->
                tikTokRepository.followUser(
                    userId = currentUserId,
                    userToFollowId = user.userInfo?.user?.id ?: "",
                    follow = true
                )
            }
            .subscribeBy(this::onError) { onComplete("FollowAllUsers completed") }
            .apply(this::addDisposable)
    }

    companion object {
        private const val TEST_CURRENT_USER_LOGIN = "volodymyrsenyk0"
        private const val TEST_TARGET_USER_LOGIN = "markiv_anastasia"

        private const val CURRENT_USER_KEY = "current"
        private const val TARGET_USER_KEY = "target"
    }
}
