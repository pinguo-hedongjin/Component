package com.kubi.sdk

import android.view.View
import androidx.fragment.app.Fragment
import com.kubi.sdk.widget.TitleBar
import com.kubi.sdk.widget.loading.ILoadingView
import com.kubi.sdk.widget.loading.LoadingDialog
import com.kubi.sdk.widget.loading.WrapperLoadingView
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

    val titleBar: TitleBar by lazy { (activity as BaseActivity).titleBar }
    private val loadingDialog: LoadingDialog by lazy { LoadingDialog(activity!!) }
    private val loadingView: ILoadingView by lazy { WrapperLoadingView.injectView(view) }

    fun showLoadingDialog() {
        loadingDialog.show()
    }

    fun hideLoadingDialog() {
        loadingDialog.dismiss()
    }

    fun showLoadingView() {
        loadingView.showLoading()
    }

    fun showContentView() {
        loadingView.showContent()
    }

    fun showEmptyView(tips: Int = R.string.fail_default, icon: Int = R.mipmap.ic_empty, click: View.OnClickListener? = null) {
        loadingView.showEmpty(tips, icon, click)
    }

    fun showFailView(tips: Int = R.string.fail_default, click: View.OnClickListener? = null) {
        loadingView.showError(tips, click)
    }

    fun runOnUiThread(block: () -> Unit, delay: Long = 0) {
        Flowable.timer(delay, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { block() }
                .addTo(disposable)
    }

    override fun onBackPressed() = false

    override fun onDestroyView() {
        super.onDestroyView()
        disposable.dispose()
    }
}