package com.kubi.router.interceptor

import com.kubi.router.model.Postcard

/**
 * author:  hedongjin
 * date:  2019-12-24
 * description: Please contact me if you have any questions
 */
interface IChain {
    fun postcard(): Postcard

    fun proceed(postcard: Postcard): Any?
}