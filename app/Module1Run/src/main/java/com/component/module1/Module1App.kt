package com.component.module1

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import android.util.Log
import com.kubi.router.core.Router
import com.kubi.router.core.RouterConfig
import com.kubi.sdk.annotations.AppLifecycle
import com.kubi.sdk.delegate.IAppLifecycle

/**
 * author:  hedongjin
 * date:  2019-12-25
 * description: Please contact me if you have any questions
 */
@AppLifecycle(priority = 3)
class Module1App : IAppLifecycle {
    override fun attachBaseContext(context: Context) {
    }

    override fun onCreate(application: Application) {
        Router.init(RouterConfig.Builder().build(application))
        Log.i("component>>>", "Module1App")
    }

    override fun onTerminate(application: Application) {
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
    }

}