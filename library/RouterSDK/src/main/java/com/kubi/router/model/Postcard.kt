package com.kubi.router.model

import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import com.kubi.router.core.Router
import com.kubi.router.interceptor.IInterceptor
import com.kubi.router.ui.IResult
import com.kubi.router.utils.*
import java.io.Serializable

/**
 * author:  hedongjin
 * date:  2019-12-09
 * description: Please contact me if you have any questions
 *
 * 路由上下文信息
 */

class Postcard(val uri: Uri, val bundle: Bundle = Bundle()) {

    init {
        bundle.bind(uri)
    }

    /**
     * @hide
     */
    var result: IResult?
        get() {
            return bundle.optResultCall()
        }
        set(value) {
            bundle.withResultCall(value!!)
        }

    /**
     * @hide
     */
    var redirect: IRedirect?
        get() {
            return bundle.optRedirectCall()
        }
        set(value) {
            bundle.withRedirectCall(value!!)
        }

    /**
     * @hide
     */
    var flags: Int
        get() {
            return bundle.optLaunchFlag()
        }
        set(value) {
            bundle.withLaunchFlag(value)
        }

    /**
     * @hide
     */
    var interceptors: MutableList<IInterceptor>? = null
        private set

    fun result(result: IResult): Postcard {
        this.result = result
        return this
    }

    fun flags(flag: Int): Postcard {
        this.flags = flag
        return this
    }

    fun redirectCall(redirect: IRedirect): Postcard {
        this.redirect = redirect
        return this
    }

    fun redirectUri(url: String): Postcard {
        bundle.withRedirectUri(url)
        return this
    }

    fun greenChannel() = interceptors(mutableListOf())

    fun interceptors(interceptors: MutableList<IInterceptor>): Postcard {
        this.interceptors = interceptors
        return this
    }

    fun appendParamter(key: String, value: Any?): Postcard {
        if (value == null) return this

        when (value) {
            // 基础类型
            is Byte -> bundle.withByte(key, value)
            is Short -> bundle.withShort(key, value)
            is Int -> bundle.withInt(key, value)
            is Long -> bundle.withLong(key, value)
            is Float -> bundle.withFloat(key, value)
            is Double -> bundle.withDouble(key, value)
            is Boolean -> bundle.withBoolean(key, value)
            is String -> bundle.withString(key, value)
            is Class<*> -> bundle.withString(key, value.name)
            // 序列化类型
            is Bundle -> bundle.withBundle(key, value)
            is Parcelable -> bundle.withParcelable(key, value)
            is Serializable -> bundle.withSerializable(key, value)
            // 其他类型
            else -> bundle.withJson(key, value)
        }

        return this
    }

    fun appendParamter(map: Map<String, Any>?): Postcard {
        map?.entries?.forEach { appendParamter(it.key, it.value) }
        return this
    }


    fun getRedirectUri(): String {
        return Uri.Builder().apply {
            scheme(uri.scheme)
            authority(uri.authority)
            path(uri.path)

            bundle.keySet().forEach { key ->
                bundle.optString(key)?.let { value ->
                    appendQueryParameter(key, value)
                }
            }

        }.build().toString()

    }

    fun navigation() = Router.navigation(this)
}