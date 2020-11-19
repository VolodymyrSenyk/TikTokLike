package com.senyk.volodymyr.tiktoklike.di.module

import com.senyk.volodymyr.tiktoklike.di.module.activity.main.MainActivityFragmentsContributor
import com.senyk.volodymyr.tiktoklike.di.module.activity.main.MainActivityModule
import com.senyk.volodymyr.tiktoklike.presentation.view.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module(includes = [ViewModelModule::class])
interface ActivitiesContributor {

    @ContributesAndroidInjector(
        modules = [
            MainActivityModule::class,
            MainActivityFragmentsContributor::class
        ]
    )
    fun contributeMainActivity(): MainActivity
}
