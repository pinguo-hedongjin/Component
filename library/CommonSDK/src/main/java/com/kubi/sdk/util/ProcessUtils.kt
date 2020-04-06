package com.kubi.sdk.util

import android.app.ActivityManager
import android.app.Application
import android.content.Context
import android.os.Process
import android.text.TextUtils
import java.io.BufferedReader
import java.io.File
import java.io.FileReader

/**
 * author:  hedongjin
 * date:  2020/3/3
 * description: Please contact me if you have any questions
 */
object ProcessUtils {

    fun isMainProcess(context: Context) = context.packageName == getProcessName(context)

    fun getProcessName(context: Context): String {
        var processName = getCurrentProcessNameByFile()
        if (!TextUtils.isEmpty(processName)) return processName!!

        processName = getCurrentProcessNameByAms(context)
        if (!TextUtils.isEmpty(processName)) return processName!!

        return getCurrentProcessNameByReflect(context) ?: ""
    }

    private fun getCurrentProcessNameByFile(): String? {
        return try {
            BufferedReader(FileReader(File("/proc/" + Process.myPid() + "/" + "cmdline"))).use {
                it.readLine().trim { it <= ' ' }
            }
        } catch (e: Exception) {
            null
        }
    }

    private fun getCurrentProcessNameByAms(context: Context): String? {
        val am = context.getSystemService(Context.ACTIVITY_SERVICE) as? ActivityManager ?: return null

        val processInfos = am.runningAppProcesses
        if (processInfos == null || processInfos.isEmpty()) return null

        return processInfos.find { it.pid == Process.myPid() && it.processName != null }?.processName
    }

    private fun getCurrentProcessNameByReflect(context: Context): String? {
        return try {
            val app: Application = context.applicationContext as Application
            val loadedApkField = app.javaClass.getField("mLoadedApk")
            loadedApkField.isAccessible = true

            val loadedApk = loadedApkField[app]
            val activityThreadField = loadedApk.javaClass.getDeclaredField("mActivityThread")
            activityThreadField.isAccessible = true

            val activityThread = activityThreadField[loadedApk]
            val getProcessName = activityThread.javaClass.getDeclaredMethod("getProcessName")
            getProcessName.invoke(activityThread) as String
        } catch (e: Exception) {
            null
        }
    }

}