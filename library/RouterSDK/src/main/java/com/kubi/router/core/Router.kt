package com.kubi.router.core

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import com.kubi.router.interceptor.IChain
import com.kubi.router.interceptor.IInterceptor
import com.kubi.router.interceptor.InterceptorChain
import com.kubi.router.model.*
import com.kubi.router.ui.HolderActivity
import com.kubi.router.ui.Request
import com.kubi.router.utils.*
import java.lang.reflect.Proxy
import java.util.regex.Pattern
import kotlin.RuntimeException

/**
 * author:  hedongjin
 * date:  2019-12-09
 * description: Please contact me if you have any questions
 */

private const val ROUTE_FRAGMENT = "fragment"
private const val ROUTE_SERVICE = "service"
private const val DEFAULT_ACTIVITY = "com.kubi.sdk.BaseActivity"

// 外层包装Activity
const val ROUTE_ACTIVITY = "activity"

@SuppressLint("StaticFieldLeak")
object Router {

    private lateinit var config: RouterConfig

    private val routes = hashMapOf<String, Class<*>>()

    private val services = hashMapOf<String, Any>()

    private val interceptors = mutableListOf<IInterceptor>().apply {
        add(DefaultInterceptor())
    }

    fun init(_config: RouterConfig) {
        config = _config
        LogUtils.setDebug(_config.debug)
        ContextUtils.init(_config.context)

        Register().apply {
            injectRoute(routes)
            injectService(services)
            injectInterceptor(interceptors)
        }
    }

    fun build(uriString: String) = build(uriString, null)

    fun build(uriString: String, bundle: Bundle?): Postcard {
        val schemeUriString = if (uriString.hasScheme()) {
            uriString
        } else {
            "${config.internal}://${uriString}"
        }
        return Postcard(Uri.parse(schemeUriString), bundle ?: Bundle())
    }

    fun redirect(bundle: Bundle?): Boolean {
        return bundle?.let {
            val resultUri = bundle.optRedirectUri()?.let {
                LogUtils.i("redirectUri = $it")
                build(it).navigation()
                true
            } ?: false

            val resultCall = bundle.optRedirectCall()?.let {
                LogUtils.i("redirectCall = $it")
                it.redirect()
                true
            } ?: false

            resultUri || resultCall
        } ?: false
    }

    fun redirect(intent: Intent?) = redirect(intent?.extras)

    fun navigation(postcard: Postcard): Any? {
        val interceptors = postcard.interceptors?.apply {
            add(0, DefaultInterceptor())
        } ?: interceptors

        return InterceptorChain(interceptors, interceptors.size - 1, postcard).proceed(postcard)
    }

    fun getService(uriString: String) = build(uriString).navigation() as IService

    fun <T> getService(cls: Class<T>): T {
        val annotation = cls.getAnnotation(com.kubi.router.annotation.Proxy::class.java) ?: throw RuntimeException("没有声明Proxy注解")
        val remoteService = getService(annotation.module + "/" + annotation.path) as ServiceProxy
        return Proxy.newProxyInstance(cls.classLoader, arrayOf(cls)) { proxy, method, args ->
            remoteService.invoke(method.name, args ?: arrayOf())
        } as T
    }

    private fun _navigation(postcard: Postcard): Any? {
        val schema = postcard.uri.scheme
        val action = postcard.uri.action
        val context = checkNotNull(ContextUtils.getTopActivityOrApp()) { "没有初始化" }

        LogUtils.i("navigation uri = ${postcard.uri}")

        return if (schema == config.internal) {
            when {
                routes.containsKey("$ROUTE_ACTIVITY://$action") -> {
                    val intent = Intent(context, routes["$ROUTE_ACTIVITY://$action"]).apply {
                        putExtras(postcard.bundle)
                    }

                    context.launch(intent, postcard)

                    true
                }
                routes.containsKey("$ROUTE_FRAGMENT://$action") -> {
                    val intent = Intent(context, Class.forName(postcard.bundle.optString(ROUTE_ACTIVITY, DEFAULT_ACTIVITY))).apply {
                        putExtra(ROUTE_FRAGMENT, routes["$ROUTE_FRAGMENT://$action"]!!.name)
                        putExtras(postcard.bundle)
                    }

                    context.launch(intent, postcard)

                    true
                }
                services.containsKey("$ROUTE_SERVICE://$action") -> {
                    ServiceProxy(services["$ROUTE_SERVICE://$action"]!!)
                }
                else -> config.handler(RuntimeException("未找到对应路由:$action"))
            }
        } else {
            context.launch(Intent(Intent.ACTION_VIEW, postcard.uri), postcard)
        }
    }

    private fun Context.launch(realIntent: Intent, postcard: Postcard) {
        val intent = if (postcard.result == null) {
            realIntent
        } else {
            HolderActivity.addRequest(Request(realIntent, postcard.result))
            Intent(this, HolderActivity::class.java)
        }


        if (this !is Activity) {
            intent.flags = postcard.flags or Intent.FLAG_ACTIVITY_NEW_TASK
        } else {
            intent.flags = postcard.flags
        }

        startActivity(intent)
    }

    private fun String.hasScheme(): Boolean {
        val pattern = Pattern.compile("[a-zA-Z_][a-zA-Z0-9]*://")
        val matcher = pattern.matcher(this)
        return matcher.find() && matcher.start() == 0
    }


    private class DefaultInterceptor : IInterceptor {
        override fun intercept(chain: IChain) = _navigation(chain.postcard())
    }
}


class RouterConfig(val context: Context, val internal: String, val debug: Boolean = false, val handler: (Throwable) -> Unit) {
    class Builder {

        private var debug: Boolean = true

        private var internal: String = "internal"

        private var handler: ((Throwable) -> Unit)? = null


        fun setDebug(debug: Boolean): Builder {
            this.debug = debug
            return this
        }

        fun setInternal(internal: String): Builder {
            this.internal = internal
            return this
        }

        fun setErrorHandler(handler: (Throwable) -> Unit): Builder {
            this.handler = handler
            return this
        }

        fun build(context: Context): RouterConfig {

            if (handler == null) {
                handler = { throw it }
            }

            return RouterConfig(context, internal, debug, handler!!)
        }

    }
}