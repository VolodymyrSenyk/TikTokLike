package com.senyk.volodymyr.tiktoklike.presentation.viewmodel.model

import com.senyk.volodymyr.tiktoklike.domain.TikTokRepository
import com.senyk.volodymyr.tiktoklike.presentation.viewmodel.base.BaseRxViewModel
import io.reactivex.rxkotlin.subscribeBy
import javax.inject.Inject

class SharedViewModel @Inject constructor(
    private val tikTokRepository: TikTokRepository
) : BaseRxViewModel() {

    fun onCookieGotClick(cookie: String) {
        onSetLikeClick(cookie)
    }

    fun onGetUserDetailsClick(cookie: String) {
        tikTokRepository.getUserDetails(cookie = cookie, userId = "volodymyrsenyk0")
            .subscribeBy(this::onError) { onComplete("GetUserDetails completed") }
            .apply(this::addDisposable)
    }

    fun onGetVideosClick(cookie: String) {
        tikTokRepository.getTrendingStream(cookie = cookie, userId = "6892969089498612741")
            .subscribeBy(this::onError) { onComplete("GetVideos completed") }
            .apply(this::addDisposable)
    }

    fun onSetLikeClick(cookie: String) {
        tikTokRepository.setLike(
            cookie = cookie,
            userId = "6892969089498612741",
            videoId = "6893087314021485825",
            like = true
        )
            .subscribeBy(this::onError) { onComplete("SetLike completed") }
            .apply(this::addDisposable)
    }

    fun onRemoveLikeClick(cookie: String) {
        tikTokRepository.setLike(
            cookie = cookie,
            videoId = "6893087314021485825",
            userId = "6892969089498612741",
            like = false
        )
            .subscribeBy(this::onError) { onComplete("RemoveLike completed") }
            .apply(this::addDisposable)
    }
}
