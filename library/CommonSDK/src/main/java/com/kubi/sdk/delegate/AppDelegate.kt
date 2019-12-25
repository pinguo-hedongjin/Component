package com.kubi.sdk.delegate

import android.app.Application
import android.content.Context
import android.content.res.Configuration

/**
 * author:  hedongjin
 * date:  2019-09-18
 * description: Please contact me if you have any questions
 */
class AppDelegate : IAppLifecycle {

    private val injectAppList= mutableListOf<IAppLifecycle>()
    private val injectActivityList = mutableListOf<IActivityLifecycle>()
    private val injectFragmentList = mutableListOf<IFragmentLifecycle>()

    private val fragmentDelegate = FragmentDelegate(injectFragmentList)
    private val activityDelegate = ActivityDelegate(fragmentDelegate, injectActivityList)

    override fun attachBaseContext(context: Context) {
        with(RegisterLifecycle()) {
            injectAppLifecycle(injectAppList)
            injectActivityLifecycle(injectActivityList)
            injectFragmentLifecycle(injectFragmentList)
        }

        injectAppList.forEach {
            it.attachBaseContext(context)
        }
    }

    override fun onCreate(application: Application) {
        application.registerActivityLifecycleCallbacks(activityDelegate)

        injectAppList.forEach {
            it.onCreate(application)
        }
    }

    override fun onTerminate(application: Application) {
        application.unregisterActivityLifecycleCallbacks(activityDelegate)

        injectAppList.forEach {
            it.onTerminate(application)
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        injectAppList.forEach {
            it.onConfigurationChanged(newConfig)
        }
    }
}