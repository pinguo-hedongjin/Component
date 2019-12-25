package com.kubi.sdk

/**
 * author:  hedongjin
 * date:  2019-09-18
 * description: Please contact me if you have any questions
 *
 * 子类覆盖{@link BaseLazyFragment} lazyLoadData可快速实现Fragment赖加载
 */
abstract class BaseLazyFragment : BaseFragment() {

    private var isViewCreated = false // 界面是否已创建完成
    private var isVisibleToUser = false // 是否对用户可见
    private var isDataLoaded = false // 数据是否已请求

    abstract fun lazyLoadData()

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        this.isVisibleToUser = isVisibleToUser
        tryLoadData()
    }

    override fun onResume() {
        super.onResume()
        this.isViewCreated = true
        tryLoadData()
    }

    private fun tryLoadData() {
        if (isViewCreated && isVisibleToUser && !isDataLoaded && isParentVisible()) {
            lazyLoadData()
            isDataLoaded = true
            dispatchParentVisibleState()
        }
    }

    /***
     * ViewPager场景下，判断父fragment是否可见
     */
    private fun isParentVisible(): Boolean {
        return parentFragment == null || ((parentFragment as? BaseLazyFragment)?.isVisibleToUser ?: false)
    }

    /***
     * ViewPager场景下，当前fragment可见时，如果其子fragment也可见，则让子fragment请求数据
     */
    private fun dispatchParentVisibleState() {
        childFragmentManager.fragments.filter {
            it is BaseLazyFragment && it.isVisibleToUser
        }.map {
            it as BaseLazyFragment
        }.forEach {
            it.tryLoadData()
        }
    }
}