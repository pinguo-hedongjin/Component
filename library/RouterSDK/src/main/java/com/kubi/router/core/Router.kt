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
        add(object : IInterceptor {
            override fun intercept(chain: IChain): Any? {
                return _navigation(chain.postcard())
            }

        })
    }

    fun init(_context: Context) {
        context = _context

        Register().apply {
            injectRoute(routes)
            injectService(services)
            injectInterceptor(interceptors)
        }
    }

    /***
     * 跳转页面
     * @param uri Uri fragment://lever/market?coin=kcs
     * @param callback IResult 是否使用startActivityForResult启动
     */
    fun navigation(uri: Uri, callback: IResult? = null): Any? {
        checkNotNull(context) { LogUtils.e("请初始化后，在使用Router") }
        return navigation(context!!, uri, callback)
    }

    fun navigation(context: Context, uri: Uri, callback: IResult? = null): Any? {
        return navigation(Postcard.Builder().setContext(context).setResult(callback).build(uri))
    }

    fun navigation(postcard: Postcard): Any? {
        return if (!postcard.greenChannel) {
            InterceptorChain(interceptors, interceptors.size - 1, postcard).proceed(postcard)
        } else {
            _navigation(postcard)
        }
    }

    private fun _navigation(postcard: Postcard): Any? {
        return when (postcard.route) {
            ROUTE_ACTIVITY -> {
                checkNotNull(routes[postcard.action]) { "没有找到相关路由" }

                val context = postcard.context ?: checkNotNull(context) { "没有初始化" }

                val intent = Intent(context, routes[postcard.action]!!).apply {
                    putExtras(Bundle().bind(postcard.uri))
                }

                context.launch(intent, postcard)

                true
            }
            ROUTE_FRAGMENT -> {
                checkNotNull(routes[postcard.action]) { "没有找到相关路由" }

                val context = postcard.context ?: checkNotNull(context) { "没有初始化" }

                val intent = Intent(context, Class.forName("com.kubi.sdk.BaseActivity")).apply {
                    putExtra(ROUTE_FRAGMENT, routes[postcard.action]!!.name)
                    putExtras(Bundle().bind(postcard.uri))
                }

                context.launch(intent, postcard)

                true
            }
            ROUTE_SERVICE -> {
                checkNotNull(services[postcard.action]) { "没有找到相关服务" }
                ServiceProxy(services[postcard.action]!!)
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

}