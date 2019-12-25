package com.component.module2

import androidx.annotation.Keep
import com.kubi.router.annotation.Service

/**
 * author:  hedongjin
 * date:  2019-12-25
 * description: Please contact me if you have any questions
 */

@Keep
@Service(module = "module2", path = "home")
class Module2Service {
    fun getModuleName() = "我的名字:Module2"
}