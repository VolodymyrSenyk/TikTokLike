package com.senyk.volodymyr.tiktoklike.di.module.activity.main

import com.senyk.volodymyr.tiktoklike.presentation.view.MainActivity
import com.senyk.volodymyr.tiktoklike.presentation.view.base.BaseActivity
import dagger.Binds
import dagger.Module

@Module
interface MainActivityModule {

    @Binds
    fun bindActivity(activity: MainActivity): BaseActivity
}
