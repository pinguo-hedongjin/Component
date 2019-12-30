package com.component.demo

import android.os.Bundle
import com.kubi.sdk.BaseActivity

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportFragmentManager.beginTransaction()
            .add(android.R.id.content, MainFragment())
            .addToBackStack(null)
            .commitAllowingStateLoss()
    }
}
