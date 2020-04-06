package com.kubi.router.utils

import android.os.Bundle
import android.os.Parcelable
import java.io.Serializable

/**
 * author:  hedongjin
 * date:  2020-02-16
 * description: Please contact me if you have any questions
 */
object BundleUtils {

    fun optByte(bundle: Bundle, key: String) = bundle.optByte(key)

    fun optByte(bundle: Bundle, key: String, default: Byte) = bundle.optByte(key) ?: default

    fun withByte(bundle: Bundle, key: String, value: Byte) {
        bundle.withByte(key, value)
    }

    fun optShort(bundle: Bundle, key: String) = bundle.optShort(key)

    fun optShort(bundle: Bundle, key: String, default: Short) = bundle.optShort(key) ?: default

    fun withShort(bundle: Bundle, key: String, value: Short) {
        bundle.withShort(key, value)
    }

    fun optInt(bundle: Bundle, key: String) = bundle.optInt(key)

    fun optInt(bundle: Bundle, key: String, default: Int) = bundle.optInt(key) ?: default

    fun withInt(bundle: Bundle, key: String, value: Int) {
        bundle.withInt(key, value)
    }

    fun optLong(bundle: Bundle, key: String) = bundle.optLong(key)

    fun optLong(bundle: Bundle, key: String, default: Long) = bundle.optLong(key) ?: default

    fun withLong(bundle: Bundle, key: String, value: Long) {
        bundle.withLong(key, value)
    }

    fun optFloat(bundle: Bundle, key: String) = bundle.optFloat(key)

    fun optFloat(bundle: Bundle, key: String, default: Float) = bundle.optFloat(key)
            ?: default

    fun withFloat(bundle: Bundle, key: String, value: Float) {
        bundle.withFloat(key, value)
    }

    fun optDouble(bundle: Bundle, key: String) = bundle.optDouble(key)

    fun optDouble(bundle: Bundle, key: String, default: Double) = bundle.optDouble(key)
            ?: default

    fun withDouble(bundle: Bundle, key: String, value: Double) {
        bundle.withDouble(key, value)
    }

    fun optString(bundle: Bundle, key: String) = bundle.optString(key)

    fun optString(bundle: Bundle, key: String, default: String) = bundle.optString(key)
            ?: default

    fun withString(bundle: Bundle, key: String, value: String) {
        bundle.withString(key, value)
    }

    fun optBoolean(bundle: Bundle, key: String) = bundle.optBoolean(key)

    fun optBoolean(bundle: Bundle, key: String, default: Boolean) = bundle.optBoolean(key)
            ?: default

    fun withBoolean(bundle: Bundle, key: String, value: Boolean) {
        bundle.withBoolean(key, value)
    }

    fun optBundle(bundle: Bundle, key: String) = bundle.optBundle(key)

    fun withBundle(bundle: Bundle, key: String, value: Bundle) {
        bundle.withBundle(key, value)
    }

    fun <T : Parcelable> optParcelable(bundle: Bundle, key: String) = bundle.optParcelable<T>(key)

    fun withParcelable(bundle: Bundle, key: String, value: Parcelable) {
        bundle.withParcelable(key, value)
    }

    fun <T : Serializable> optSerializable(bundle: Bundle, key: String) = bundle.optSerializable<T>(key)

    fun withSerializable(bundle: Bundle, key: String, value: Serializable) {
        bundle.withSerializable(key, value)
    }

    inline fun <reified T : Any> optFromJson(bundle: Bundle, key: String) = bundle.optFromJson<T>(key)

    fun withJson(bundle: Bundle, key: String, value: Any) {
        bundle.withJson(key, value)
    }
}