package com.senyk.volodymyr.tiktoklike.di.component

import android.content.Context
import com.senyk.volodymyr.tiktoklike.App
import com.senyk.volodymyr.tiktoklike.di.module.ActivitiesContributor
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        ActivitiesContributor::class
    ]
)
@Singleton
interface AppComponent : AndroidInjector<App> {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun context(context: Context): Builder

        fun build(): AppComponent
    }
}
