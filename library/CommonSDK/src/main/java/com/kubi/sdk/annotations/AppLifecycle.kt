package com.kubi.sdk.annotations

/**
 * author:  hedongjin
 * date:  2019-09-18
 * description: Please contact me if you have any questions
 *
 * priority: 越大越先初始化，全局不可重复
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.BINARY)
annotation class AppLifecycle(val priority: Int = 0)