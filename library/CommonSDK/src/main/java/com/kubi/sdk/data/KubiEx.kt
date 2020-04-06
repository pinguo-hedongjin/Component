package com.kubi.sdk.data

import io.reactivex.Flowable
import io.reactivex.exceptions.Exceptions
import io.reactivex.internal.functions.ObjectHelper
import io.reactivex.internal.subscriptions.DeferredScalarSubscription
import org.reactivestreams.Subscriber
import java.util.concurrent.Callable
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.TimeUnit

/**
 * author:  hedongjin
 * date:  2020-01-20
 * description: Please contact me if you have any questions
 */
val SUCCESS = Any()

fun fromRunnable(block: () -> Unit): Flowable<Any> {
    return fromCallable {
        block()
        SUCCESS
    }
}

fun <T> fromCallable(callable: () -> T): Flowable<T> {
    return FixFromCallable(callable)
}

fun <T> get(block: ((Any) -> Unit) -> Unit): T {
    val queue = LinkedBlockingQueue<Any>(1)


    block { queue.add(it) }

    return when (val result = queue.poll(10, TimeUnit.MINUTES)) {
        null -> throw GetException("get执行超时10秒")
        is Throwable -> throw RuntimeException(result.message)
        else -> result as T
    }

}

fun safe(block: () -> Unit) {
    try {
        block()
    } catch (e: Throwable) {
        e.printStackTrace()
    }
}

class GetException(msg: String) : RuntimeException(msg)

class FixFromCallable<T>(private val callable: () -> T) : Flowable<T>(), Callable<T> {

    public override fun subscribeActual(s: Subscriber<in T>) {
        val deferred = DeferredScalarSubscription(s)
        s.onSubscribe(deferred)
        if (deferred.isCancelled) {
            return
        }

        val t = try {
            ObjectHelper.requireNonNull(callable(), "The callable returned a null value")
        } catch (ex: Throwable) {
            Exceptions.throwIfFatal(ex)
            if (!deferred.isCancelled) {
                s.onError(ex)
            }
            return
        }

        deferred.complete(t)
    }

    @Throws(Exception::class)
    override fun call(): T {
        return ObjectHelper.requireNonNull(callable(), "The callable returned a null value")
    }
}