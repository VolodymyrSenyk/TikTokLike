package com.senyk.volodymyr.tiktoklike.di.module

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import com.senyk.volodymyr.tiktoklike.BuildConfig
import com.senyk.volodymyr.tiktoklike.data.datasource.*
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class NetworkModule {

    @Provides
    @Singleton
    fun getColleaguesApiInterface(retrofit: Retrofit): TikTokApi =
        retrofit.create(TikTokApi::class.java)

    @Provides
    @Singleton
    fun getOkHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor,
        headersInterceptor: Interceptor
    ): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(httpLoggingInterceptor)
        .addInterceptor(headersInterceptor)
        .build()

    @Provides
    @Singleton
    fun getHttpLoggingInterceptor(): HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
        level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
        else HttpLoggingInterceptor.Level.NONE
    }

    @Provides
    @Singleton
    fun getHeadersInterceptor(): Interceptor = Interceptor { chain ->
        val request = chain.request().newBuilder()
           // .addHeader(HEADER_USER_AGENT, HEADER_DEFAULT_USER_AGENT)
          //  .addHeader(HEADER_ACCEPT, HEADER_DEFAULT_ACCEPT)
          //  .addHeader(HEADER_CONTENT_TYPE, HEADER_DEFAULT_CONTENT_TYPE)
          //  .addHeader(HEADER_DNT, HEADER_DEFAULT_DNT)
          //  .addHeader(HEADER_ORIGIN, HEADER_DEFAULT_ORIGIN)
          //  .addHeader(HEADER_REFERER, HEADER_DEFAULT_REFERER)
            .build()
        chain.proceed(request)
    }

    @Provides
    @Singleton
    fun getRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()
}
