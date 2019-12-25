package com.kubi.sdk.delegate

import android.app.Activity
import android.os.Bundle

/**
 * author:  hedongjin
 * date:  2019-09-18
 * description: Please contact me if you have any questions
 *
 * activity生命周期声明
 */
interface IActivityLifecycle {
    fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?)
    fun onActivityStarted(activity: Activity)
    fun onActivityResumed(activity: Activity)
    fun onActivityPaused(activity: Activity)
    fun onActivityStopped(activity: Activity)
    fun onActivitySaveInstanceState(activity: Activity, outState: Bundle?)
    fun onActivityDestroyed(activity: Activity)
}