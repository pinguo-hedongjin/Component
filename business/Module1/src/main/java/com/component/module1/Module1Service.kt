package com.component.module1

import androidx.annotation.Keep
import com.kubi.router.annotation.Service

/**
 * author:  hedongjin
 * date:  2019-12-25
 * description: Please contact me if you have any questions
 */

@Keep
@Service(module = "module1", path = "home")
class Module1Service {
    fun getModuleName() = "我的名字:Module1"
}