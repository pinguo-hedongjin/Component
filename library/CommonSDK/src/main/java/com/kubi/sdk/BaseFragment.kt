package com.kubi.sdk

import androidx.fragment.app.Fragment
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import java.util.concurrent.TimeUnit

/**
 * author:  hedongjin
 * date:  2019-09-18
 * description: Please contact me if you have any questions
 *
 * 处理公共逻辑
 */
abstract class BaseFragment : Fragment(), IBackCallback {

    val TAG = this::class.java.simpleName

    val disposable = CompositeDisposable()

    fun runOnUiThread(block: () -> Unit, delay: Long = 0) {
        Flowable.timer(delay, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { block() }
                .addTo(disposable)
    }

    override fun onBackPressed() = false

    override fun onDestroyView() {
        super.onDestroyView()
        disposable.clear()
    }
}