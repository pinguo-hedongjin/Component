package com.component.module2

import android.os.Bundle
import com.kubi.sdk.BaseActivity

/**
 * author:  hedongjin
 * date:  2019-12-25
 * description: Please contact me if you have any questions
 */
class Module2Activity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportFragmentManager.beginTransaction()
            .add(android.R.id.content, Module2Fragment())
            .addToBackStack(null)
            .commitAllowingStateLoss()
    }
}