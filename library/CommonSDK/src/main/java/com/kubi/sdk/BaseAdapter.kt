package com.kubi.sdk

import android.util.SparseArray
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers

/**
 * author:  hedongjin
 * date:  2019/5/9
 * description: 用于解耦不同数据类型
 */
open class BaseAdapter<T : Any>(
        open var data: MutableList<T>? = null,
        open val proxys: SparseArray<Class<*>>,
        open val callbacks: SparseArray<ItemCallBack<*>>,
        open val diffCb: DiffCallBack<T>? = null
) : RecyclerView.Adapter<InnerViewHolder<T>>() {

    private val helper = DiffRefreshHelper()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InnerViewHolder<T> {
        val proxy = proxys[viewType].getConstructor(ViewGroup::class.java, ItemCallBack::class.java)
                .newInstance(parent, callbacks[viewType])

        return InnerViewHolder(parent, proxy as ViewHolderProxy<T>)
    }

    override fun onBindViewHolder(holder: InnerViewHolder<T>, position: Int) {
        holder.proxy.dispatchBindView(getItem(position))
    }

    fun getItem(position: Int): T {
        return data!![position]
    }

    override fun getItemCount() = if (data == null) 0 else data!!.size

    override fun getItemViewType(position: Int): Int {
        return getItem(position).javaClass.name.hashCode()
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        helper.release()
    }

    fun refresh(data: MutableList<T>?) {
        helper.refresh(data)
    }

    open class Builder<T : Any> {
        var data: MutableList<T>? = null
        var diffCb: DiffCallBack<T>? = null
        val proxys: SparseArray<Class<*>> = SparseArray()
        val callbacks: SparseArray<ItemCallBack<*>> = SparseArray()

        fun setData(data: MutableList<T>?): Builder<T> {
            this.data = data
            return this
        }

        fun setDiffCallBack(diffCb: DiffCallBack<T>): Builder<T> {
            this.diffCb = diffCb
            return this
        }

        fun register(cls: Class<*>, proxy: Class<*>, callBack: ItemCallBack<*>? = null): Builder<T> {
            proxys.put(cls.name.hashCode(), proxy)
            callBack?.let {
                callbacks.put(cls.name.hashCode(), callBack)
            }
            return this
        }

        open fun build() = BaseAdapter(data, proxys, callbacks, diffCb)
    }

    inner class DiffRefreshHelper {
        private var version = 0
        private val disposable = CompositeDisposable()

        fun refresh(newData: MutableList<T>?) {
            if (diffCb == null || data.empty() || newData.empty()) {
                data = newData
                notifyDataSetChanged()
            } else {

                val currentVersion = ++version
                Flowable.fromCallable { DiffUtil.calculateDiff(InnerCallback(data, newData, diffCb!!)) }
                        .singleElement()
                        .subscribeOn(Schedulers.computation())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe {
                            if (version == currentVersion) {
                                data = newData
                                it.dispatchUpdatesTo(this@BaseAdapter)
                            }
                        }.addTo(disposable)
            }
        }

        fun release() {
            disposable.clear()
        }


        private fun <R> MutableList<R>?.empty() = (this?.size ?: 0) == 0

    }
}

abstract class ViewHolderProxy<D>(open val parent: ViewGroup, open val callback: ItemCallBack<*>? = null) {
    var data: D? = null
    lateinit var holder: RecyclerView.ViewHolder

    fun dispatchBindView(d: D) {
        data = d
        bindView(d)
    }

    abstract fun createView(): View
    open fun bindView(d: D) = Unit
}

interface ItemCallBack<D> {
    fun callback(view: View, pos: Int, data: D)
}

class InnerViewHolder<T>(
        val parent: ViewGroup,
        val proxy: ViewHolderProxy<T>
) : RecyclerView.ViewHolder(proxy.createView()) {
    init {
        proxy.holder = this@InnerViewHolder
    }
}

class InnerCallback<T>(private var oldData: List<T>? = null, private val newData: List<T>?, val callback: DiffCallBack<T>) : DiffUtil.Callback() {

    override fun getOldListSize() = oldData?.size ?: 0

    override fun getNewListSize() = newData?.size ?: 0

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            callback.areItemsTheSame(oldItemPosition, oldData!![oldItemPosition], newItemPosition, newData!![newItemPosition])

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            callback.areContentsTheSame(oldItemPosition, oldData!![oldItemPosition], newItemPosition, newData!![newItemPosition])
}

interface DiffCallBack<T> {
    fun areItemsTheSame(oldPos: Int, oldItem: T, newPos: Int, newItem: T): Boolean
    fun areContentsTheSame(oldPos: Int, oldItem: T, newPos: Int, newItem: T): Boolean
}