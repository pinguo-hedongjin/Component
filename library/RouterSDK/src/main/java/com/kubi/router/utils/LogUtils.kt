package com.kubi.router.utils

import android.util.Log

/**
 * author:  hedongjin
 * date:  2019-12-09
 * description: Please contact me if you have any questions
 */
object LogUtils {
    private const val TAG = "Router"

    @Volatile
    private var debug: Boolean = true

    fun setDebug(_debug: Boolean) {
        debug = _debug
    }

    fun i(msg: String) {
        if (debug) {
            Log.i(TAG, msg)
        }
    }

    fun e(msg: String) {
        if (debug) {
            Log.e(TAG, msg)
        }
    }
}