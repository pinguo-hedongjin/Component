package com.component.module1

import androidx.annotation.Keep
import com.kubi.router.annotation.Method
import com.kubi.router.annotation.Route

/**
 * author:  hedongjin
 * date:  2019-12-25
 * description: Please contact me if you have any questions
 */

val TEACHER_ARRAY = arrayListOf("董俊峰", "李一平", "王洋", "文学国")

@Keep
@Route(module = "module1", path = "service", desc = "Module1")
class Module1Service {

    @Method(name = "GET_MODULE_NAME", value = "getModuleName", desc = "获取模块名")
    fun getModuleName() = "我的名字:Module1"

    @Method(name = "GET_TEACHER_NAME", value = "getTeacherName", desc = "获取老师名")
    fun getTeacherName() = TEACHER_ARRAY[java.util.Random().nextInt(TEACHER_ARRAY.size)]
}