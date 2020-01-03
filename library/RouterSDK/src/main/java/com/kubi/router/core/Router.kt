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
import com.kubi.router.ui.IResult
import com.kubi.router.ui.Request
import com.kubi.router.utils.LogUtils
import com.kubi.router.utils.action
import com.kubi.router.utils.bind
import java.lang.RuntimeException

/**
 * author:  hedongjin
 * date:  2019-12-09
 * description: Please contact me if you have any questions
 */
@SuppressLint("StaticFieldLeak")
object Router {

    @Volatile
    private var context: Context? = null

    private val routes = hashMapOf<String, Class<*>>()

    private val services = hashMapOf<String, Any>()

    private val interceptors = mutableListOf<IInterceptor>().apply {
        add(DefaultInterceptor())
    }

    fun init(_context: Context) {
        context = _context

        Register().apply {
            injectRoute(routes)
            injectService(services)
            injectInterceptor(interceptors)
        }
    }

    fun with(context: Context? = null) = Postcard.Builder()._context(context)

    fun navigation(postcard: Postcard): Any? {
        val interceptors = postcard.interceptors?.apply {
            add(0, DefaultInterceptor())
        } ?: interceptors

        return InterceptorChain(interceptors, interceptors.size - 1, postcard).proceed(postcard)
    }

    private fun _navigation(postcard: Postcard): Any? {
        val schema = postcard.uri.scheme
        val action = postcard.uri.action

        return when (schema) {
            ROUTE_ACTIVITY -> {
                checkNotNull(routes[action]) { "没有找到相关路由" }

                val context = postcard.context ?: checkNotNull(context) { "没有初始化" }

                val intent = Intent(context, routes[action]!!).apply {
                    putExtras(Bundle().bind(postcard.uri))
                }

                context.launch(intent, postcard)

                true
            }
            ROUTE_FRAGMENT -> {
                checkNotNull(routes[action]) { "没有找到相关路由" }

                val context = postcard.context ?: checkNotNull(context) { "没有初始化" }

                val intent = Intent(context, Class.forName("com.kubi.sdk.BaseActivity")).apply {
                    putExtra(ROUTE_FRAGMENT, routes[action]!!.name)
                    putExtras(Bundle().bind(postcard.uri))
                }

                context.launch(intent, postcard)

                true
            }
            ROUTE_SERVICE -> {
                checkNotNull(services[action]) { "没有找到相关服务" }
                ServiceProxy(services[action]!!)
            }
            else -> throw RuntimeException("未知的Route类型")
        }
    }

    private fun Context.launch(realIntent: Intent, postcard: Postcard) {
        val intent = if (postcard.callback == null) {
            realIntent
        } else {
            HolderActivity.addRequest(Request(realIntent, postcard.callback))
            Intent(this, HolderActivity::class.java)
        }

        if (this !is Activity) {
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }

        startActivity(intent)
    }


    private class DefaultInterceptor : IInterceptor {
        override fun intercept(chain: IChain) = _navigation(chain.postcard())
    }
}