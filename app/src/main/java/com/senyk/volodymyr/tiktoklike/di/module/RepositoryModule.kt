package com.senyk.volodymyr.tiktoklike.di.module

import com.senyk.volodymyr.tiktoklike.data.repository.CookieSharedPrefsRepository
import com.senyk.volodymyr.tiktoklike.data.repository.TikTokNetworkRepository
import com.senyk.volodymyr.tiktoklike.domain.CookieRepository
import com.senyk.volodymyr.tiktoklike.domain.TikTokRepository
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module(includes = [NetworkModule::class, PersistenceModule::class])
interface RepositoryModule {

    @Singleton
    @Binds
    fun bindCookieRepository(repository: CookieSharedPrefsRepository): CookieRepository

    @Singleton
    @Binds
    fun bindTikTokRepository(repository: TikTokNetworkRepository): TikTokRepository
}
