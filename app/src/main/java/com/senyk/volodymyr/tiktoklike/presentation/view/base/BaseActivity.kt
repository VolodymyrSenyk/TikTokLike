package com.senyk.volodymyr.tiktoklike.presentation.view.base

import android.os.Bundle
import androidx.annotation.LayoutRes
import com.senyk.volodymyr.tiktoklike.presentation.viewmodel.factory.ViewModelFactory
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

abstract class BaseActivity : DaggerAppCompatActivity() {

    @get:LayoutRes
    protected abstract val layoutRes: Int

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutRes)
    }
}
