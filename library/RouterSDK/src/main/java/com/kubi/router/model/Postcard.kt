package com.kubi.router.model

import android.content.Context
import android.net.Uri
import com.kubi.router.core.Router
import com.kubi.router.interceptor.IInterceptor
import com.kubi.router.ui.IResult

/**
 * author:  hedongjin
 * date:  2019-12-09
 * description: Please contact me if you have any questions
 *
 * 路由上下文信息
 */
class Postcard(val context: Context?, val uri: Uri, val callback: IResult? = null, val interceptors: MutableList<IInterceptor>? = null) {

    fun navigation() = Router.navigation(this)

    class Builder {
        private var context: Context? = null
        private var result: IResult? = null
        private var interceptors: MutableList<IInterceptor>? = null

        fun _context(context: Context?): Builder {
            this.context = context
            return this
        }

        fun result(result: IResult?): Builder {
            this.result = result
            return this
        }

        fun greenChannel(): Builder {
            return interceptors(mutableListOf())
        }

        fun interceptors(interceptors: MutableList<IInterceptor>): Builder {
            this.interceptors = interceptors
            return this
        }

        fun uri(uri: Uri): Postcard {
            return Postcard(context, uri, result, interceptors)
        }
    }
}

const val ROUTE_ACTIVITY = "activity"
const val ROUTE_FRAGMENT = "fragment"
const val ROUTE_SERVICE = "service"