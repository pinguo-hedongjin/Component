package com.kubi.sdk.delegate

import android.app.Application
import android.content.Context
import android.content.res.Configuration

/**
 * author:  hedongjin
 * date:  2019-09-18
 * description: Please contact me if you have any questions
 *
 * application生命周期声明
 */
interface IAppLifecycle {
    fun attachBaseContext(context: Context)
    fun onCreate(application: Application)
    fun onTerminate(application: Application)
    fun onConfigurationChanged(newConfig: Configuration)
}