package com.kubi.sdk

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.kubi.sdk.widget.TitleBar
import com.kubi.sdk.widget.loading.ILoadingView
import com.kubi.sdk.widget.loading.LoadingDialog
import com.kubi.sdk.widget.loading.WrapperLoadingView
import io.reactivex.disposables.CompositeDisposable

/**
 * author:  hedongjin
 * date:  2019-09-18
 * description: Please contact me if you have any questions
 *
 * 处理公共逻辑
 */

open class BaseActivity : AppCompatActivity() {

    val TAG = this::class.java.simpleName
    val disposable = CompositeDisposable()

    val titleBar: TitleBar by lazy { TitleBar(this) }
    val content: FrameLayout by lazy { FrameLayout(this).apply { id = R.id.content } }

    private val loadingDialog: LoadingDialog by lazy { LoadingDialog(this) }
    private val loadingView: ILoadingView by lazy { WrapperLoadingView.injectView(content) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)

        // attach布局
        attachLayout()
        // 初始化Bar
        processTitleBar()
        // 只有BaseActivity才加载fragment
        processFragment()

    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.dispose()
    }

    private fun attachLayout() {
        val layout = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL }
        layout.addView(
                titleBar.also { (it.parent as? ViewGroup)?.removeView(it) },
                FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, TitleBar.dp2px(this, 42))
        )
        layout.addView(
                content.also { (it.parent as? ViewGroup)?.removeView(it) },
                FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        )

        val contentView = findViewById<ViewGroup>(android.R.id.content)
        contentView.removeAllViews()
        contentView.addView(layout, FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT).apply {
            topMargin = if (intent?.getStringExtra(STATUS_BAR)?.toBoolean() == true) getStatusBarHeight() else 0
        })
    }

    override fun setContentView(view: View) {
        content.removeAllViews()
        content.addView(view, FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
    }

    /***
     * 不要删除该方法，会作为aop的切入点存在
     */
    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(newBase)
    }

    override fun onBackPressed() {
        if (!supportFragmentManager.dispatchBackPress()) {
            finish()
        }
    }

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

    fun hideTitleBar() {
        titleBar.hide()
    }

    fun showTitleBar() {
        titleBar.show()
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

    private fun processTitleBar() {
        titleBar.setOnNavigationListener(View.OnClickListener { onBackPressed() })
        titleBar.setTitle(intent?.getStringExtra(TITLE_TEXT))

        if (intent?.getStringExtra(TITLE_BAR)?.toBoolean() != false) {
            titleBar.show()
        } else {
            titleBar.hide()
        }
    }

    private fun processFragment() {
        if (this::class.java != BaseActivity::class.java) return

        intent.getStringExtra(FRAGMENT)?.let {
            supportFragmentManager.beginTransaction()
                    .add(R.id.content, Fragment.instantiate(this, it, intent.extras))
                    .addToBackStack(null)
                    .commitAllowingStateLoss()
        }
    }

    private fun getStatusBarHeight(): Int {
        return resources.getDimensionPixelSize(resources.getIdentifier("status_bar_height", "dimen", "android"))
    }

    companion object {
        const val TITLE_TEXT = "title_text" // 标题Title
        const val TITLE_BAR = "title_bar" // 是否显示导航栏, 显示true, 默认显示
        const val STATUS_BAR = "status_bar" // 是否显示状态栏, 显示true, 默认隐藏
        const val FRAGMENT = "fragment"

        fun launch(context: Context, fragment: Class<*>, bundle: Bundle? = null) {
            context.startActivity(Intent(context, BaseActivity::class.java).apply {
                putExtra(FRAGMENT, fragment.name)
                bundle?.let { putExtras(it) }
            })
        }
    }
}