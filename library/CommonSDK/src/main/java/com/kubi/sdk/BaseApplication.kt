package com.kubi.sdk

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import android.os.Build
import androidx.multidex.MultiDex
import com.kubi.sdk.delegate.AppDelegate

/**
 * author:  hedongjin
 * date:  2019-09-18
 * description: Please contact me if you have any questions
 */
class BaseApplication : Application() {

    private val delegate = AppDelegate()

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            MultiDex.install(base)
        }
        instance = this
        delegate.attachBaseContext(base)
    }

    override fun onCreate() {
        super.onCreate()
        delegate.onCreate(this)
    }

    override fun onTerminate() {
        super.onTerminate()
        delegate.onTerminate(this)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        delegate.onConfigurationChanged(newConfig)
    }

    companion object {
        private lateinit var instance: Application

        @JvmStatic
        fun get() = instance
    }
}