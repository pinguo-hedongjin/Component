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
 */
class FragmentDelegate(
        private val injectFragmentList: List<IFragmentLifecycle>
) : FragmentManager.FragmentLifecycleCallbacks() {

    override fun onFragmentViewCreated(fm: FragmentManager, f: Fragment, v: View, savedInstanceState: Bundle?) {
        super.onFragmentViewCreated(fm, f, v, savedInstanceState)

        injectFragmentList.forEach {
            it.onFragmentViewCreated(fm, f, v, savedInstanceState)
        }
    }

    override fun onFragmentStopped(fm: FragmentManager, f: Fragment) {
        super.onFragmentStopped(fm, f)

        injectFragmentList.forEach {
            it.onFragmentStopped(fm, f)
        }
    }

    override fun onFragmentCreated(fm: FragmentManager, f: Fragment, savedInstanceState: Bundle?) {
        super.onFragmentCreated(fm, f, savedInstanceState)

        injectFragmentList.forEach {
            it.onFragmentCreated(fm, f, savedInstanceState)
        }
    }

    override fun onFragmentResumed(fm: FragmentManager, f: Fragment) {
        super.onFragmentResumed(fm, f)

        injectFragmentList.forEach {
            it.onFragmentResumed(fm, f)
        }
    }

    override fun onFragmentAttached(fm: FragmentManager, f: Fragment, context: Context) {
        super.onFragmentAttached(fm, f, context)

        injectFragmentList.forEach {
            it.onFragmentAttached(fm, f, context)
        }
    }

    override fun onFragmentPreAttached(fm: FragmentManager, f: Fragment, context: Context) {
        super.onFragmentPreAttached(fm, f, context)
    }

    override fun onFragmentDestroyed(fm: FragmentManager, f: Fragment) {
        super.onFragmentDestroyed(fm, f)

        injectFragmentList.forEach {
            it.onFragmentDestroyed(fm, f)
        }
    }

    override fun onFragmentSaveInstanceState(fm: FragmentManager, f: Fragment, outState: Bundle) {
        super.onFragmentSaveInstanceState(fm, f, outState)

        injectFragmentList.forEach {
            it.onFragmentSaveInstanceState(fm, f, outState)
        }
    }

    override fun onFragmentStarted(fm: FragmentManager, f: Fragment) {
        super.onFragmentStarted(fm, f)

        injectFragmentList.forEach {
            it.onFragmentStarted(fm, f)
        }
    }

    override fun onFragmentViewDestroyed(fm: FragmentManager, f: Fragment) {
        super.onFragmentViewDestroyed(fm, f)

        injectFragmentList.forEach {
            it.onFragmentViewDestroyed(fm, f)
        }
    }

    override fun onFragmentPreCreated(fm: FragmentManager, f: Fragment, savedInstanceState: Bundle?) {
        super.onFragmentPreCreated(fm, f, savedInstanceState)
    }

    override fun onFragmentActivityCreated(fm: FragmentManager, f: Fragment, savedInstanceState: Bundle?) {
        super.onFragmentActivityCreated(fm, f, savedInstanceState)

        injectFragmentList.forEach {
            it.onFragmentActivityCreated(fm, f, savedInstanceState)
        }
    }

    override fun onFragmentPaused(fm: FragmentManager, f: Fragment) {
        super.onFragmentPaused(fm, f)

        injectFragmentList.forEach {
            it.onFragmentPaused(fm, f)
        }
    }

    override fun onFragmentDetached(fm: FragmentManager, f: Fragment) {
        super.onFragmentDetached(fm, f)

        injectFragmentList.forEach {
            it.onFragmentDetached(fm, f)
        }
    }
}