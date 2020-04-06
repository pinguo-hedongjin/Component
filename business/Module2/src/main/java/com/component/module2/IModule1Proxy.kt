package com.component.module2

import androidx.annotation.Keep
import com.kubi.router.annotation.Proxy
import com.kubi.router.annotation.Route

/**
 * author:  hedongjin
 * date:  2019-12-25
 * description: Please contact me if you have any questions
 */

@Keep
@Proxy(module = "module1", path = "service", desc = "Module1")
interface IModule1Proxy {

    fun getModuleName(): String

    fun getTeacherName(): String
}