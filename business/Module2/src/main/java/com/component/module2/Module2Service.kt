package com.component.module2

import androidx.annotation.Keep
import com.kubi.router.annotation.Method
import com.kubi.router.annotation.Route

/**
 * author:  hedongjin
 * date:  2019-12-25
 * description: Please contact me if you have any questions
 */

@Keep
@Route(module = "module2", path = "service", desc = "Module2")
class Module2Service {

    @Method(name = "GET_MODULE_NAME", value = "getModuleName", desc = "获取模块名")
    fun getModuleName() = "我的名字:Module2"
}