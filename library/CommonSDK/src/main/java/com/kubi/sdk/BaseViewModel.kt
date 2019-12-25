package com.kubi.sdk

import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable

/**
 * author:  hedongjin
 * date:  2019-10-18
 * description: Please contact me if you have any questions
 */
abstract class BaseViewModel : ViewModel() {
    val TAG = this.javaClass.simpleName
    val disposable = CompositeDisposable()

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}