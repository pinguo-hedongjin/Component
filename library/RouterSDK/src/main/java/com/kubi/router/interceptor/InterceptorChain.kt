package com.kubi.router.interceptor

import com.kubi.router.model.Postcard
import java.lang.IndexOutOfBoundsException

/**
 * author:  hedongjin
 * date:  2019-12-09
 * description: Please contact me if you have any questions
 */
class InterceptorChain(private val interceptors: List<IInterceptor>, private val index: Int, private val postcard: Postcard) : IChain {
    override fun postcard() = postcard

    override fun proceed(postcard: Postcard): Any? {
        if (index < 0 || index >= interceptors.size) {
            throw IndexOutOfBoundsException("下标越界")
        }

        return interceptors[index].intercept(InterceptorChain(interceptors, index - 1, postcard))

    }
}