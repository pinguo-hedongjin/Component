package com.kubi.router.model

/**
 * author:  hedongjin
 * date:  2020-02-18
 * description: Please contact me if you have any questions
 */


interface IRedirect {
    fun redirect()

    companion object {
        fun obtain(block: () -> Unit): IRedirect {
            return object: IRedirect {
                override fun redirect() {
                    block()
                }
            }
        }
    }
}