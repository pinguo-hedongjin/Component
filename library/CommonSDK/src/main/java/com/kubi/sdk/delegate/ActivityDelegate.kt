package com.kubi.sdk.delegate

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.fragment.app.FragmentActivity

/**
 * author:  hedongjin
 * date:  2019-09-18
 * description: Please contact me if you have any questions
 */
class ActivityDelegate(
        private val fragmentDelegate: FragmentDelegate,
        private val injectActivityList: List<IActivityLifecycle>
) : Application.ActivityLifecycleCallbacks {

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        injectActivityList.forEach {
            it.onActivityCreated(activity, savedInstanceState)
        }

        if (activity is FragmentActivity) {
            activity.supportFragmentManager.registerFragmentLifecycleCallbacks(fragmentDelegate, true)
        }
    }

    override fun onActivityStarted(activity: Activity) {
        injectActivityList.forEach {
            it.onActivityStarted(activity)
        }
    }

    override fun onActivityResumed(activity: Activity) {
        injectActivityList.forEach {
            it.onActivityResumed(activity)
        }
    }

    override fun onActivityPaused(activity: Activity) {
        injectActivityList.forEach {
            it.onActivityPaused(activity)
        }
    }


    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        injectActivityList.forEach {
            it.onActivitySaveInstanceState(activity, outState)
        }
    }

    override fun onActivityStopped(activity: Activity) {
        injectActivityList.forEach {
            it.onActivityStopped(activity)
        }
    }


    override fun onActivityDestroyed(activity: Activity) {
        injectActivityList.forEach {
            it.onActivityDestroyed(activity)
        }
    }



}