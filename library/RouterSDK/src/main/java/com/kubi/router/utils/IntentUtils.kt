package com.kubi.router.utils

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import java.io.Serializable

/**
 * author:  hedongjin
 * date:  2020-02-16
 * description: Please contact me if you have any questions
 */
object IntentUtils {

    fun optByte(intent: Intent, key: String) = intent.optByte(key)

    fun optByte(intent: Intent, key: String, default: Byte) = intent.optByte(key, default)

    fun withByte(intent: Intent, key: String, value: Byte) {
        intent.withByte(key, value)
    }

    fun optShort(intent: Intent, key: String) = intent.optShort(key)

    fun optShort(intent: Intent, key: String, default: Short) = intent.optShort(key, default)

    fun withShort(intent: Intent, key: String, value: Short) {
        intent.withShort(key, value)
    }

    fun optInt(intent: Intent, key: String) = intent.optInt(key)

    fun optInt(intent: Intent, key: String, default: Int) = intent.optInt(key, default)

    fun withInt(intent: Intent, key: String, value: Int) {
        intent.withInt(key, value)
    }

    fun optLong(intent: Intent, key: String) = intent.optLong(key)

    fun optLong(intent: Intent, key: String, default: Long) = intent.optLong(key, default)

    fun withLong(intent: Intent, key: String, value: Long) {
        intent.withLong(key, value)
    }

    fun optFloat(intent: Intent, key: String) = intent.optFloat(key)

    fun optFloat(intent: Intent, key: String, default: Float) = intent.optFloat(key)
            ?: default

    fun withFloat(intent: Intent, key: String, value: Float) {
        intent.withFloat(key, value)
    }

    fun optDouble(intent: Intent, key: String) = intent.optDouble(key)

    fun optDouble(intent: Intent, key: String, default: Double) = intent.optDouble(key)
            ?: default

    fun withDouble(intent: Intent, key: String, value: Double) {
        intent.withDouble(key, value)
    }

    fun optString(intent: Intent, key: String) = intent.optString(key)

    fun optString(intent: Intent, key: String, default: String) = intent.optString(key)
            ?: default

    fun withString(intent: Intent, key: String, value: String) {
        intent.withString(key, value)
    }

    fun optBoolean(intent: Intent, key: String) = intent.optBoolean(key)

    fun optBoolean(intent: Intent, key: String, default: Boolean) = intent.optBoolean(key)
            ?: default

    fun withBoolean(intent: Intent, key: String, value: Boolean) {
        intent.withBoolean(key, value)
    }

    fun optBundle(intent: Intent, key: String) = intent.optBundle(key)

    fun withBundle(intent: Intent, key: String, value: Bundle) {
        intent.withBundle(key, value)
    }

    fun <T : Parcelable> optParcelable(intent: Intent, key: String) = intent.optParcelable<T>(key)

    fun withParcelable(intent: Intent, key: String, value: Parcelable) {
        intent.withParcelable(key, value)
    }

    fun <T : Serializable> optSerializable(intent: Intent, key: String) = intent.optSerializable<T>(key)

    fun withSerializable(intent: Intent, key: String, value: Serializable) {
        intent.withSerializable(key, value)
    }

    inline fun <reified T : Any> optFromJson(intent: Intent, key: String) = intent.optFromJson<T>(key)

    fun withJson(intent: Intent, key: String, value: Any) {
        intent.withJson(key, value)
    }


}