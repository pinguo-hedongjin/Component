package com.kubi.router.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import com.kubi.router.R
import com.kubi.router.ui.Request
import com.kubi.router.ui.IResult
import android.os.Parcel
import android.os.Parcelable
import android.text.TextUtils
import android.util.Base64
import com.google.gson.Gson
import com.kubi.router.core.Router
import com.kubi.router.model.IRedirect
import com.kubi.router.ui.HolderActivity
import java.lang.ref.WeakReference
import java.net.URLDecoder
import java.net.URLEncoder
import java.util.*
import kotlin.collections.ArrayList
import java.io.*


/**
 * author:  hedongjin
 * date:  2019-12-11
 * description: Please contact me if you have any questions
 */
private const val RESULT_CALL = "result_call"
private const val REDIRECT_CALL = "redirect_call"
private const val REDIRECT_URI = "redirect_uri"
private const val LAUNCH_FLAG = "launch_flag"

val Uri.action: String
    get() = "${authority}${path}"

fun Intent.optByte(key: String) = extras?.optByte(key)

fun Intent.optByte(key: String, default: Byte) = extras?.optByte(key) ?: default

fun Intent.withByte(key: String, value: Byte) = checkExtras { withByte(key, value) }

fun Intent.optShort(key: String) = extras?.optShort(key)

fun Intent.optShort(key: String, default: Short) = extras?.optShort(key) ?: default

fun Intent.withShort(key: String, value: Short) = checkExtras { withShort(key, value) }

fun Intent.optInt(key: String) = extras?.optInt(key)

fun Intent.optInt(key: String, default: Int) = extras?.optInt(key) ?: default

fun Intent.withInt(key: String, value: Int) = checkExtras { withInt(key, value) }

fun Intent.optLong(key: String) = extras?.optLong(key)

fun Intent.optLong(key: String, default: Long) = extras?.optLong(key) ?: default

fun Intent.withLong(key: String, value: Long) = checkExtras { withLong(key, value) }

fun Intent.optFloat(key: String) = extras?.optFloat(key)

fun Intent.optFloat(key: String, default: Float) = extras?.optFloat(key) ?: default

fun Intent.withFloat(key: String, value: Float) = checkExtras { withFloat(key, value) }

fun Intent.optDouble(key: String) = extras?.optDouble(key)

fun Intent.optDouble(key: String, default: Double) = extras?.optDouble(key) ?: default

fun Intent.withDouble(key: String, value: Double) = checkExtras { withDouble(key, value) }

fun Intent.optString(key: String) = extras?.optString(key)

fun Intent.optString(key: String, default: String) = extras?.optString(key) ?: default

fun Intent.withString(key: String, value: String) = checkExtras { withString(key, value) }

fun Intent.optBoolean(key: String) = extras?.optBoolean(key)

fun Intent.optBoolean(key: String, default: Boolean) = extras?.optBoolean(key) ?: default

fun Intent.withBoolean(key: String, value: Boolean) = checkExtras { withBoolean(key, value) }

fun Intent.optBundle(key: String) = extras?.optBundle(key)

fun Intent.withBundle(key: String, value: Bundle) = checkExtras { withBundle(key, value) }

fun <T : Parcelable> Intent.optParcelable(key: String): T? = extras?.optParcelable<T>(key)

fun Intent.withParcelable(key: String, value: Parcelable) = checkExtras { withParcelable(key, value) }

fun <T : Serializable> Intent.optSerializable(key: String) = extras?.optSerializable<T>(key)

fun Intent.withSerializable(key: String, value: Serializable) = checkExtras { withSerializable(key, value) }

inline fun <reified T : Any> Intent.optFromJson(key: String) = extras?.optFromJson<T>(key)

fun Intent.withJson(key: String, value: Any) = checkExtras { withJson(key, value) }

fun Intent.optResultCall() = extras?.optResultCall()

fun Intent.withResultCall(call: IResult) = extras?.withResultCall(call)

fun Intent.optRedirectCall() = extras?.optRedirectCall()

fun Intent.withRedirectCall(call: IRedirect) = extras?.withRedirectCall(call)

fun Intent.hasRedirectUri()= extras?.hasRedirectUri() == true

fun Intent.optRedirectUri() = extras?.optRedirectUri()

fun Intent.withRedirectUri(url: String) = checkExtras { withRedirectUri(url) }

fun Bundle.optByte(key: String) = opt(key) { toByte() }

fun Bundle.optByte(key: String, default: Byte) = opt(key, default) { toByte() }

fun Bundle.withByte(key: String, value: Byte) = withString(key, value.toString())

fun Bundle.optShort(key: String) = opt(key) { toShort() }

fun Bundle.optShort(key: String, default: Short) = opt(key, default) { toShort() }

fun Bundle.withShort(key: String, value: Short) = with(key, value.toString())

fun Bundle.optInt(key: String) = opt(key) { toInt() }

fun Bundle.optInt(key: String, default: Int) = opt(key, default) { toInt() }

fun Bundle.withInt(key: String, value: Int) = with(key, value.toString())

fun Bundle.optLong(key: String) = opt(key) { toLong() }

fun Bundle.optLong(key: String, default: Long) = opt(key, default) { toLong() }

fun Bundle.withLong(key: String, value: Long) = with(key, value.toString())

fun Bundle.optFloat(key: String) = opt(key) { toFloat() }

fun Bundle.optFloat(key: String, default: Float) = opt(key, default) { toFloat() }

fun Bundle.withFloat(key: String, value: Float) = with(key, value.toString())

fun Bundle.optDouble(key: String) = opt(key) { toDouble() }

fun Bundle.optDouble(key: String, default: Double) = opt(key, default) { toDouble() }

fun Bundle.withDouble(key: String, value: Double) = with(key, value.toString())

fun Bundle.optString(key: String) = opt(key) { toString() }

fun Bundle.optString(key: String, default: String) = opt(key, default) { toString() }

fun Bundle.withString(key: String, value: String) = with(key, value)

fun Bundle.optBoolean(key: String) = opt(key) { toBoolean() }

fun Bundle.optBoolean(key: String, default: Boolean) = opt(key, default) { toBoolean() }

fun Bundle.withBoolean(key: String, value: Boolean) = with(key, value.toString())

fun Bundle.optBundle(key: String) = opt(key) { decodeToParcelable<Bundle>() }

fun Bundle.withBundle(key: String, value: Bundle) = with(key, value.encodeToString())

fun <T : Parcelable> Bundle.optParcelable(key: String): T? = opt(key) { decodeToParcelable<T>() }

fun Bundle.withParcelable(key: String, value: Parcelable) = with(key, value.encodeToString())

fun <T : Serializable> Bundle.optSerializable(key: String) = opt(key) { decodeToSerializable<T>() }

fun Bundle.withSerializable(key: String, value: Serializable) = with(key, value.encodeToString())

inline fun <reified T : Any> Bundle.optFromJson(key: String) = opt<T>(key) { Gson().fromJson(this, T::class.java) }

fun Bundle.withJson(key: String, value: Any) = with(key, Gson().toJson(value))

fun Bundle.optLaunchFlag() = optInt(LAUNCH_FLAG, 0)

fun Bundle.withLaunchFlag(flag: Int) = withInt(LAUNCH_FLAG, optLaunchFlag() or flag)

fun Bundle.optResultCall() = optInt(RESULT_CALL)?.let { WeakCache.get<IResult>(it) }

fun Bundle.withResultCall(call: IResult) = withInt(RESULT_CALL, System.identityHashCode(call)).apply { WeakCache.add(call) }

fun Bundle.optRedirectCall() = optInt(REDIRECT_CALL)?.let { WeakCache.get<IRedirect>(it) }

fun Bundle.withRedirectCall(call: IRedirect) = withInt(REDIRECT_CALL, System.identityHashCode(call)).apply { WeakCache.add(call) }

fun Bundle.hasRedirectUri()= !TextUtils.isEmpty(optString(REDIRECT_URI))

fun Bundle.optRedirectUri() = optString(REDIRECT_URI)?.decodeUrl()

fun Bundle.withRedirectUri(url: String) = withString(REDIRECT_URI, url.encodeUrl())

fun Bundle.toMap() = hashMapOf<String, Any>().also {
    keySet().forEach {key->
        get(key)?.let { value ->
            it.put(key, value)
        }
    }
}

/***
 * 编码
 * @hide
 */
private fun String.encodeUrl() = URLEncoder.encode(this, "UTF-8" )

/***
 * 解码
 * @hide
 */
private fun String.decodeUrl() = URLDecoder.decode(this, "UTF-8" )

/**
 * @hide
 */
private fun <T> Bundle.opt(key: String, default: T, map: String.() -> T): T {
    return getString(key)?.map() ?: default
}

/**
 * @hide
 */
fun <T> Bundle.opt(key: String, map: String.() -> T): T? {
    return getString(key)?.map()
}

/**
 * @hide
 */
fun Bundle.with(key: String, value: String): Bundle {
    putString(key, value)
    return this
}

/**
 * @hide
 */
fun Bundle.bind(uri: Uri): Bundle {
    uri.queryParameterNames.forEach {
        putString(it, uri.getQueryParameter(it))
    }
    return this
}

/***
 * Parcelable编码成字符串
 * @receiver Bundle
 * @return String
 *
 * @hide
 */
private fun Parcelable.encodeToString(): String {
    return Parcel.obtain().use {
        it.writeParcelable(this, 0)
        Base64.encodeToString(it.marshall(), Base64.DEFAULT)
    }
}

/***
 * string解码成Parcelable
 * @receiver String
 * @return Bundle
 *
 * @hide
 */
private fun <T: Parcelable> String.decodeToParcelable(): T {
    val bytes = Base64.decode(this, Base64.DEFAULT)
    return Parcel.obtain().use {
        it.unmarshall(bytes, 0, bytes.size)
        it.setDataPosition(0)
        it.readParcelable(Router::class.java.classLoader)
    }
}

/***
 * Serializable编码成字符串
 * @receiver Serializable
 * @return String
 *
 * @hide
 */
fun Serializable.encodeToString(): String {
    return ByteArrayOutputStream().use {

        ObjectOutputStream(it).use { it.writeObject(this) }

        Base64.encodeToString(it.toByteArray(), Base64.DEFAULT)
    }
}

/***
 * string解码成Serializable
 * @receiver String
 * @return Bundle
 *
 * @hide
 */
fun <T: Serializable> String.decodeToSerializable(): T {
    val bytes = Base64.decode(this, Base64.DEFAULT)

    return ByteArrayInputStream(bytes).use {
        ObjectInputStream(it).use {
            it.readObject()
        }
    } as T
}

fun Activity.launchForResult(realIntent: Intent, result: IResult) {
    val intent = Intent(this, HolderActivity::class.java)
    result.addTo(this)

    HolderActivity.addRequest(Request(realIntent, result))
    startActivity(intent)
}

/**
 * @hide
 */
private fun Intent.checkExtras(block: Bundle.() -> Unit): Intent {
    if (extras == null) {
        putExtras(Bundle())
    }
    extras!!.block()
    return this
}

/***
 * Request中IResult是弱引用，必须重新绑定引用链，防止被GC
 * @hide
 */
fun <T> T.addTo(activity: Activity): T {
    var cache = activity.findViewById<View>(android.R.id.content).let {
        it.tag ?: ArrayList<T>().apply {
            it.setTag(R.id.route_activity_result, this)
        }

    } as MutableList<T>

    cache.add(this)

    return this
}

/**
 * @hide
 */
fun <T> Parcel.use(block: (Parcel) -> T): T {
    try {
        return block(this)
    } finally {
        recycle()
    }
}

/**
 * @hide
 */
private object WeakCache {
    private val cache = LinkedList<WeakReference<Any>>()

    @Synchronized
    fun add(callback: Any) {
        val topActivity = ContextUtils.getTopActivityOrApp() as? Activity
        if (topActivity == null) {
            LogUtils.e("没有任何activity跟当前callback绑定")
        } else {
            callback.addTo(topActivity)
        }
        cache.add(WeakReference(callback))
    }

    @Synchronized
    fun remove(hashCode: Int) {
        val iterator = cache.iterator()
        while (iterator.hasNext()) {
            if (System.identityHashCode(iterator.next().get()) == hashCode) {
                iterator.remove()
            }
        }
    }

    @Synchronized
    fun <T> get(hashCode: Int): T? {
        val iterator = cache.iterator()
        while (iterator.hasNext()) {
            val callback = iterator.next().get()
            if (callback == null) {
                iterator.remove()
            } else if (System.identityHashCode(callback) == hashCode) {
                return callback as T
            }
        }

        return null
    }
}
