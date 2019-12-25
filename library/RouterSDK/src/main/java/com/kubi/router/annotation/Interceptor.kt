package com.kubi.router.annotation

/**
 * author:  hedongjin
 * date:  2019-12-09
 * description: Please contact me if you have any questions
 *
 * 注册拦截器的注解
 *
 * priority: 越大拦截等级越高，全局不可重复
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.BINARY)
annotation class Interceptor(val priority: Int)