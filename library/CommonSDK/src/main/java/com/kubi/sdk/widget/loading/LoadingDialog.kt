package com.kubi.sdk.widget.loading

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import com.kubi.sdk.R
import java.util.concurrent.atomic.AtomicInteger

/**
 * author:  hedongjin
 * date:  2020/3/26
 * description: Please contact me if you have any questions
 */
class LoadingDialog(context: Context) : Dialog(context) {

    private val loadingCount = AtomicInteger(0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.view_loading)

        setCancelable(false)
        setCanceledOnTouchOutside(false)

        window.setGravity(Gravity.CENTER)
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    @Synchronized
    override fun show() {
        if (loadingCount.getAndIncrement() == 0) {
            super.show()
        }
    }

    @Synchronized
    override fun dismiss() {
        if (loadingCount.get() == 0) {
            return
        }

        if (loadingCount.decrementAndGet() == 0) {
            super.dismiss()
        }
    }

    @Synchronized
    override fun isShowing(): Boolean {
        return loadingCount.get() > 0
    }
}