package com.component.module1

import android.os.Bundle
import com.kubi.sdk.BaseActivity

/**
 * author:  hedongjin
 * date:  2019-12-25
 * description: Please contact me if you have any questions
 */
class Module1Activity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportFragmentManager.beginTransaction()
            .add(android.R.id.content, Module1Fragment())
            .addToBackStack(null)
            .commitAllowingStateLoss()
    }
}