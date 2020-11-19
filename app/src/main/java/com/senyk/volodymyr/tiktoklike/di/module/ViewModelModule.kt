package com.senyk.volodymyr.tiktoklike.di.module

import androidx.lifecycle.ViewModel
import com.senyk.volodymyr.tiktoklike.di.annotation.mapkey.ViewModelKey
import com.senyk.volodymyr.tiktoklike.presentation.viewmodel.model.SharedViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module(includes = [RepositoryModule::class])
interface ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(SharedViewModel::class)
    fun bindSharedViewModel(viewModel: SharedViewModel): ViewModel
}
