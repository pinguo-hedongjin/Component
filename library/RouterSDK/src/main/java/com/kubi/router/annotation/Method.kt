package com.kubi.router.annotation

/**
 * author:  hedongjin
 * date:  2019-12-09
 * description: Please contact me if you have any questions
 *
 * 生成代码注解
 *
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
annotation class Method(val name: String, val value: String, val desc: String)