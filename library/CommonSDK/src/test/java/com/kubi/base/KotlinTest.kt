package com.kubi.base

import android.os.Build
import androidx.annotation.RequiresApi
import kotlinx.coroutines.*
import kotlinx.coroutines.time.delay
import org.junit.Test
import java.time.Duration
import kotlin.system.measureTimeMillis

/**
 * author:  hedongjin
 * date:  2020-01-19
 * description: Please contact me if you have any questions
 */
@RequiresApi(Build.VERSION_CODES.O)
class KotlinTest {

    //=============================== 基础 ===============================
    @Test
    fun demo1() = runBlocking {
        //主线程会一直阻塞到内部协程完毕
        val job = GlobalScope.launch {
            //全局的协程
            delay(Duration.ofSeconds(1L))
            println("World!")
        }

        print("Hello ")
//        delay(Duration.ofSeconds(2L)) // 延迟2秒，保证jvm存活
        job.join() // 等待子协程执行结束
    }

    @Test
    fun demo2() = runBlocking {
        // 结构化并发
        launch {
            delay(Duration.ofSeconds(1L))
            println("World!")
        }

        println("Hello ")
    }

    @Test
    fun demo3() = runBlocking {
        launch {
            delay(Duration.ofSeconds(2L))
            println("Task from runBlocking")
        }

        coroutineScope {
            // 创建协程作用域, 跟runBlocking类似，但是是挂起
            launch {
                delay(Duration.ofSeconds(5L))
                println("Task from nested launch")
            }

            delay(Duration.ofSeconds(1L))
            println("Task from coroutine scope")
        }

        println("Coroutine scope  is over")
    }

    @Test
    fun demo4() = runBlocking {
        launch { doWorld() }

        println("Hello, ")
    }

    // 挂起函数
    suspend fun doWorld() {
        delay(Duration.ofSeconds(1L))
        println("World!")
    }

    @Test
    fun demo5() = runBlocking {
        repeat(1000_000) {
            launch {
                delay(Duration.ofSeconds(1L))
                println(".")
            }
        }
    }

    @Test
    fun demo6() = runBlocking {
        GlobalScope.launch {
            repeat(1000) {
                delay(Duration.ofSeconds(1L))
                println("I am sleeping $it ...")
            }
        }

        delay(Duration.ofSeconds(4))
    }

    //=============================== 取消与超时 ===============================
    @Test
    fun demo7() = runBlocking {
        // 只有挂起函数可以被取消
        val job = launch {
            try {
                repeat(100_000) {
                    println("job:I am sleeping $it ...")
                    delay(Duration.ofSeconds(1L))
                }
            } finally { // finally运行挂起函数，需要withContext包装
                withContext(NonCancellable) {
                    doWorld()
                }
            }
        }

        delay(Duration.ofSeconds(4L))

        println("main:I am tired of waiting!")
        job.cancelAndJoin()

        println("main:now i can quit!")

    }

    @Test
    fun demo8() = runBlocking {
        val job = launch(Dispatchers.Default) {
            var nextPrintTime = System.currentTimeMillis()
            var i = 0
            while (isActive) {
                if (System.currentTimeMillis() > nextPrintTime) {
                    println("job:I am sleeping ${i++} ...")
                    nextPrintTime += 1000
                }
            }

        }

        delay(Duration.ofSeconds(4L))
        println("main:wait")
        job.cancelAndJoin()
        println("main:quit")
    }

    @Test
    fun demo9() = runBlocking {
        val result = withTimeoutOrNull(3000L) {
            repeat(5_000) {
                println("I am sleeping $it ...")
                delay(Duration.ofSeconds(1))
            }
            "Done"
        }

        println("Result is $result")
    }


    //=============================== 取消与超时 ===============================
    suspend fun doSomethingUsefulOne(): Int {
        delay(Duration.ofSeconds(1L))
        return 13
    }

    suspend fun doSomethingUsefulTwo(): Int {
        delay(Duration.ofSeconds(1L))
        return 29
    }

    @Test
    fun demo10() = runBlocking {
        val time = measureTimeMillis {
            val one = async { doSomethingUsefulOne() } // 并发
            val two = async { doSomethingUsefulTwo() } // 并发
            println("The answer is ${one.await() + two.await()}")
        }

        println("Completed in $time ms")
    }

    @Test
    fun demo11() = runBlocking {
        val time = measureTimeMillis {
            val one = async(start = CoroutineStart.LAZY) {
                // 惰性启动
                doSomethingUsefulOne()
            }

            val two = async(start = CoroutineStart.LAZY) {
                doSomethingUsefulTwo()
            }

            one.start() // 启动
            two.start()

            println("The answer is ${one.await() + two.await()}")
        }

        println("Completed is $time ms")
    }

    @Test
    fun demo12() = runBlocking(Dispatchers.IO) {

        launch {
            println("runBlocking:${Thread.currentThread().name}")
        }

        launch(Dispatchers.Default) {
            println("default:${Thread.currentThread().name}")
        }

        launch(Dispatchers.IO) {
            println("io:${Thread.currentThread().name}")
        }

//        launch(Dispatchers.Main) {
//            print("main:${Thread.currentThread().name}")
//        }

        launch(Dispatchers.Unconfined) {
            println("unconfined:${Thread.currentThread().name}")
        }

        launch(newSingleThreadContext("MyThread")) {
            // 启动新线程，不需要时需要close
            println("my:${Thread.currentThread().name}")
        }

        delay(Duration.ofSeconds(2L))
    }

    @Test
    fun demo13() = runBlocking {
        launch(Dispatchers.Unconfined) {
            // 只运用到第一个挂起点
            println("Unconfined:${Thread.currentThread().name}")
            delay(Duration.ofSeconds(1L))
            println("Unconfined:${Thread.currentThread().name}")
        }

        launch {
            println("Default:${Thread.currentThread().name}")
            delay(Duration.ofSeconds(1L))
            println("Default:${Thread.currentThread().name}")
        }

        println("Done")
    }

    @Test
    fun demo14() {
        newSingleThreadContext("Thread1").use { t1 ->
            newSingleThreadContext("Thread2").use { t2 ->
                runBlocking(t1) {
                    println("Thread1 start")
                }

                runBlocking(t2) {
                    println("Thread2 start")
                }

                println("Done")
            }
        }
    }

    @Test
    fun demo15() = runBlocking {
        val job = launch {
            GlobalScope.launch {// 不会被取消掉
                println("job1:start")
                delay(Duration.ofSeconds(2L))
                println("job1:end")
            }

            launch {
                println("job2:start")
                delay(Duration.ofSeconds(2L))
                println("job2:end")
            }
        }

        delay(Duration.ofSeconds(1L))
        job.cancel()

        delay(Duration.ofSeconds(2L))
        println("Done")
    }


}