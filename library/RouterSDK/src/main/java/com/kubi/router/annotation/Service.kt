package com.kubi.router.annotation

/**
 * author:  hedongjin
 * date:  2019-10-14
 * description: Please contact me if you have any questions
 *
 * 注册服务的注解
 */

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.BINARY)
annotation class Service (val module: String, val path: String)