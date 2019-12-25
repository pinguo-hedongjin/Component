package com.component.demo

import androidx.annotation.Keep
import com.kubi.router.annotation.Service

/**
 * author:  hedongjin
 * date:  2019-12-25
 * description: Please contact me if you have any questions
 */

@Keep
@Service(module = "app", path = "home")
class MainService {
    fun getModuleName() = "我的名字:App"
}