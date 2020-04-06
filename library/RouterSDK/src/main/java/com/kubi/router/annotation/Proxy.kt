package com.kubi.router.annotation

/**
 * author:  hedongjin
 * date:  2019-12-09
 * description: Please contact me if you have any questions
 *
 * 注册路由的注解
 *
 * module: 模块
 * path: 路径
 * module/path组合全局不可重复
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Proxy(val module: String, val path: String, val desc: String)