package com.kubi.router.utils

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.kubi.router.R
import com.kubi.router.ui.IResult

/**
 * author:  hedongjin
 * date:  2019-12-11
 * description: Please contact me if you have any questions
 */

fun Uri.Builder.route(route: String) = scheme(route)

fun Uri.Builder.module(module: String) = authority(module)

fun Uri.Builder.appendParam(key: String, value: Any) = appendQueryParameter(key, value.toString())

val Uri.route: String?
    get() = scheme

val Uri.module: String?
    get() = authority

val Uri.action: String
    get() = "${module}${path}"

fun <T> Bundle.opt(key: String, default: T, map: String.() -> T): T {
    return getString(key)?.map() ?: default
}

fun <T> Bundle.opt(key: String, map: String.() -> T): T? {
    return getString(key)?.map()
}

fun Bundle.bind(uri: Uri): Bundle {
    uri.queryParameterNames.forEach {
        putString(it, uri.getQueryParameter(it))
    }
    return this
}

/***
 * Request中IResult是弱引用，必须重新绑定引用链，防止被GC
 */
fun IResult.addTo(activity: Activity): IResult {
    var cache = activity.findViewById<View>(android.R.id.content).let {
        it.tag ?: ArrayList<IResult>().apply {
            it.setTag(R.id.route_activity_result, this)
        }

    } as MutableList<IResult>

    cache.add(this)

    return this
}

fun IResult.addTo(fragment: Fragment): IResult {
    var cache = fragment.view!!.let {
        it.tag ?: ArrayList<IResult>().apply {
            it.setTag(R.id.route_fragment_result, this)
        }

    } as MutableList<IResult>

    cache.add(this)

    return this
}

