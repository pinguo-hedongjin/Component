package com.kubi.router.utils

/**
 * author:  hedongjin
 * date:  2019-09-23
 * description: Please contact me if you have any questions
 */
class IoUtils {


    static void close(Closeable... closeables) {
        closeables.each {
            try {
                it.close()
            } catch (IOException e) {

            }
        }
    }
}