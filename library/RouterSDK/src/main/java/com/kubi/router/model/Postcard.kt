package com.kubi.router.model

import android.content.Context
import android.net.Uri
import com.kubi.router.ui.IResult
import com.kubi.router.utils.module
import com.kubi.router.utils.route

/**
 * author:  hedongjin
 * date:  2019-12-09
 * description: Please contact me if you have any questions
 *
 * 路由上下文信息
 */
class Postcard(val context: Context?, val uri: Uri, val greenChannel: Boolean, val callback: IResult? = null) {

    val route: String = uri.route!!

    val action: String = "${uri.module}${uri.path}"

    class Builder {
        private var context: Context? = null
        private var result: IResult? = null
        private var greenChannel = false

        fun setContext(context: Context?): Builder {
            this.context = context
            return this
        }

        fun setResult(result: IResult?): Builder {
            this.result = result
            return this
        }

        fun setGreenChannel(greenChannel: Boolean): Builder {
            this.greenChannel = greenChannel
            return this
        }

        fun build(uri: Uri): Postcard {
            return Postcard(context, uri, greenChannel, result)
        }
    }
}

const val ROUTE_ACTIVITY = "activity"
const val ROUTE_FRAGMENT = "fragment"
const val ROUTE_SERVICE = "service"