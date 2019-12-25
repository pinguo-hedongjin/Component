package com.kubi.router.interceptor

/**
 * author:  hedongjin
 * date:  2019-12-09
 * description: Please contact me if you have any questions
 */
interface IInterceptor {

    fun intercept(chain: IChain): Any?
}