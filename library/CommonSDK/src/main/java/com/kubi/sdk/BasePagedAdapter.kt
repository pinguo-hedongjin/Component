package com.kubi.sdk

import android.util.SparseArray
import android.view.ViewGroup
import androidx.paging.PagedList
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil

/**
 * author:  hedongjin
 * date:  2019/5/9
 * description: 用于解耦不同数据类型
 */
open class BasePagedAdapter<T : Any>(
        open val proxys: SparseArray<Class<*>>,
        open val callbacks: SparseArray<ItemCallBack<*>>,
        open val diffCb: DiffUtil.ItemCallback<T>,
        open val refresh: ((Int) -> Unit)?
) : PagedListAdapter<T, InnerViewHolder<T>>(diffCb) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InnerViewHolder<T> {
        val proxy = proxys[viewType].getConstructor(ViewGroup::class.java, ItemCallBack::class.java)
                .newInstance(parent, callbacks[viewType])

        return InnerViewHolder(parent, proxy as ViewHolderProxy<T>)
    }

    override fun onBindViewHolder(holder: InnerViewHolder<T>, position: Int) {
        holder.proxy.dispatchBindView(getItem(position)!!)
    }


    override fun getItemViewType(position: Int): Int {
        return getItem(position)!!.javaClass.name.hashCode()
    }

    override fun getItem(position: Int): T? {
        return super.getItem(position).apply {
            refresh?.invoke(itemCount)
        }
    }


    override fun submitList(pagedList: PagedList<T>?) {
        super.submitList(pagedList)
        notifyDataSetChanged()
    }

    open class Builder<T : Any> {
        val proxys: SparseArray<Class<*>> = SparseArray()
        val callbacks: SparseArray<ItemCallBack<*>> = SparseArray()
        var refresh: ((Int) -> Unit)? = null

        fun setRefreshListener(refresh: (Int) -> Unit): Builder<T> {
            this.refresh = refresh
            return this
        }

        fun register(cls: Class<*>, proxy: Class<*>, callBack: ItemCallBack<*>? = null): Builder<T> {
            proxys.put(cls.name.hashCode(), proxy)
            callBack?.let {
                callbacks.put(cls.name.hashCode(), callBack)
            }
            return this
        }

        open fun build(diffCb: DiffUtil.ItemCallback<T>) = BasePagedAdapter(proxys, callbacks, diffCb, refresh)
    }
}