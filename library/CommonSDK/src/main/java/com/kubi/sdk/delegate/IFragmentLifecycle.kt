package com.kubi.sdk.delegate

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

/**
 * author:  hedongjin
 * date:  2019-09-18
 * description: Please contact me if you have any questions
 *
 * fragment生命周期声明
 */
interface IFragmentLifecycle {

    fun onFragmentAttached(fm: FragmentManager, f: Fragment, context: Context)

    fun onFragmentCreated(fm: FragmentManager, f: Fragment, savedInstanceState: Bundle?)

    fun onFragmentActivityCreated(fm: FragmentManager, f: Fragment, savedInstanceState: Bundle?)

    fun onFragmentViewCreated(fm: FragmentManager, f: Fragment, v: View, savedInstanceState: Bundle?)

    fun onFragmentStarted(fm: FragmentManager, f: Fragment)

    fun onFragmentResumed(fm: FragmentManager, f: Fragment)

    fun onFragmentPaused(fm: FragmentManager, f: Fragment)

    fun onFragmentStopped(fm: FragmentManager, f: Fragment)

    fun onFragmentSaveInstanceState(fm: FragmentManager, f: Fragment, outState: Bundle)

    fun onFragmentViewDestroyed(fm: FragmentManager, f: Fragment)

    fun onFragmentDestroyed(fm: FragmentManager, f: Fragment)

    fun onFragmentDetached(fm: FragmentManager, f: Fragment)
}