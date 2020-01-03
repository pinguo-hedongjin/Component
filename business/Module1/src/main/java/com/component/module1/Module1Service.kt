package com.component.module1

import androidx.annotation.Keep
import com.kubi.router.annotation.Service
import kotlin.random.Random

/**
 * author:  hedongjin
 * date:  2019-12-25
 * description: Please contact me if you have any questions
 */

val TEACHER_ARRAY = arrayListOf("董俊峰", "李一平", "王洋", "文学国")

@Keep
@Service(module = "module1", path = "home")
class Module1Service {

    fun getModuleName() = "我的名字:Module1"

    fun getTeacherName() = TEACHER_ARRAY[java.util.Random().nextInt(TEACHER_ARRAY.size)]
}