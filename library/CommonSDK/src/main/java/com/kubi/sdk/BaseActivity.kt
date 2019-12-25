package com.kubi.sdk

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

/**
 * author:  hedongjin
 * date:  2019-09-18
 * description: Please contact me if you have any questions
 *
 * 处理公共逻辑
 */
open class BaseActivity : AppCompatActivity() {

    val TAG = this::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)

        processFragment()
    }

    override fun onBackPressed() {
        if (!supportFragmentManager.dispatchBackPress()) {
            if (supportFragmentManager?.fragments?.size ?: 0 <= 1) {
                finish()
            } else {
                super.onBackPressed()
            }
        }
    }

    private fun FragmentManager.dispatchBackPress(): Boolean {
        if (fragments.isNotEmpty()) {
            for (index in fragments.indices.reversed()) {
                val fragment = fragments[index] ?: continue

                if (fragment.childFragmentManager.dispatchBackPress()) {
                    return true
                }

                if (fragment.isResumed && fragment.isVisible && fragment.userVisibleHint
                        && fragment is IBackCallback && (fragment as IBackCallback).onBackPressed()) {
                    return true
                }
            }
        }
        return false
    }

    private fun processFragment() {
        intent.getStringExtra("fragment")?.let {
            supportFragmentManager.beginTransaction()
                    .add(android.R.id.content, instantiate(it, intent.extras))
                    .addToBackStack(null)
                    .commitAllowingStateLoss()
        }
    }

    fun instantiate(fragmentName: String, bundle: Bundle? = null): Fragment {
        val clazz = classLoader.loadClass(fragmentName)
        val instance = clazz.getConstructor().newInstance() as Fragment

        bundle?.let {
            instance.arguments = bundle
        }

        return instance
    }


}