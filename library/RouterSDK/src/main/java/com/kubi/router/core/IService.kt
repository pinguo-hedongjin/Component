package com.kubi.router.core

import java.lang.reflect.Method

/**
 * author:  hedongjin
 * date:  2019-10-14
 * description: Please contact me if you have any questions
 */
interface IService {

    /****
     * 调用服务提供的方法
     * @param method String
     * @param args Array<out Any>
     */
    fun <T> call(method: String, vararg args: Any?): T?

}

internal class ServiceProxy(private val service: Any) : IService {

    override fun <T> call(method: String, vararg args: Any?): T? {
        return findMethod(method, args).call(service, args) as T?
    }

    fun <T> invoke(method: String, args: Array<out Any?>): T? {
        return findMethod(method, args).call(service, args) as T?
    }

    private fun findMethod(methodName: String, args: Array<out Any?>): Method {
        val methods = service.javaClass.declaredMethods
        for (method in methods) {
            if (method.name != methodName) {
                continue
            }

            val paramTypes = method.parameterTypes
            if (paramTypes.size != args.size) {
                continue
            }

            var match = true
            for (index in args.indices) {
                var cls = paramTypes[index]
                when (cls) {
                    Integer.TYPE -> {
                        cls = Integer::class.java
                    }
                    java.lang.Short.TYPE -> {
                        cls = java.lang.Short::class.java
                    }
                    java.lang.Double.TYPE -> {
                        cls = java.lang.Double::class.java
                    }
                    java.lang.Float.TYPE -> {
                        cls = java.lang.Float::class.java
                    }
                    java.lang.Long.TYPE -> {
                        cls = java.lang.Long::class.java
                    }
                    java.lang.Boolean.TYPE -> {
                        cls = java.lang.Boolean::class.java
                    }
                    java.lang.Byte.TYPE -> {
                        cls = java.lang.Byte::class.java
                    }
                }

                if (args[index] != null && !cls.isAssignableFrom(args[index]!!::class.java)) {
                    match = false
                    break
                }
            }

            if (match) {
                return method
            }

        }

        throw RuntimeException("服务${service.javaClass.name}中不存在方法${methodName}")
    }

    private fun Method.call(service: Any, args: Array<out Any?>): Any? {
        return when {
            args.isEmpty() -> invoke(service)
            args.size == 1 -> invoke(service, args[0])
            args.size == 2 -> invoke(service, args[0], args[1])
            args.size == 3 -> invoke(service, args[0], args[1], args[2])
            args.size == 4 -> invoke(service, args[0], args[1], args[2], args[3])
            args.size == 5 -> invoke(service, args[0], args[1], args[2], args[3], args[4])
            args.size == 6 -> invoke(service, args[0], args[1], args[2], args[3], args[4], args[5])
            args.size == 7 -> invoke(service, args[0], args[1], args[2], args[3], args[4], args[5], args[6])
            args.size == 8 -> invoke(service, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7])
            args.size == 9 -> invoke(service, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7], args[8])
            args.size == 10 -> invoke(service, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7], args[8], args[9])
            else -> throw RuntimeException("不支持的参数长度")
        }
    }
}