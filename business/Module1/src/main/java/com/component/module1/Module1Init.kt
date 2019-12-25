package com.component.module1

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import android.util.Log
import android.widget.Toast
import com.kubi.router.core.Router
import com.kubi.sdk.annotations.AppLifecycle
import com.kubi.sdk.delegate.IAppLifecycle

/**
 * author:  hedongjin
 * date:  2019-12-25
 * description: Please contact me if you have any questions
 */
@AppLifecycle
class Module1Init : IAppLifecycle {
    override fun attachBaseContext(context: Context) {
    }

    override fun onCreate(application: Application) {
        Log.i("component>>>", "Module1Init")
    }

    override fun onTerminate(application: Application) {
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
    }

}