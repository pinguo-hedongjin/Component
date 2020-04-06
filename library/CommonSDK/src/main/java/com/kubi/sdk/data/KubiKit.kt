package com.kubi.sdk.data

import io.reactivex.schedulers.Schedulers

/**
 * author:  hedongjin
 * date:  2019-09-19
 * description: Please contact me if you have any questions
 */
object KubiKit {

    const val TAG = "KubiKit"

    /***
     * 服务map映射
     */
    private val services = HashMap<Class<*>, Any>()

    /***
     * 获取服务接口
     */
    fun <T> getService(key: Class<T>) = services[key] as T

    fun registerService(key: Class<*>, value: Any) {
        registerService(Pair(key, value))
    }

    fun registerService(vararg pairs: Pair<Class<*>, Any>) {
        // 注册服务
        pairs.forEach { services[it.first] = it.second }

        // 初始化服务
        pairs.filter { it.second is IInternal }.map { it.second as IInternal }.forEach {
            Schedulers.io().scheduleDirect {
                it.init()
            }
        }
    }
}

/***
 * 内部服务接口,init在异步线程调用
 */
interface IInternal {

    fun init()
}

