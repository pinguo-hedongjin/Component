package com.kubi.sdk.widget.loading;

import android.view.View;
import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;

/**
 * @author <a href="emperor@kucoin.com">tql</a >
 * @version 2.0
 * @description
 * @time 2018/9/4
 */

public interface ILoadingView {
    /**
     * 显示loading
     */
    void showLoading();

    /**
     * 显示loading
     */
    void showLoading(int gravity);

    /**
     * 显示内容
     */
    void showContent();

    /***
     * 显示空
     */
    void showEmpty(@StringRes int res1, @DrawableRes int res2, View.OnClickListener onClickListener);

    /**
     * 显示错误，并且提供点击回调
     */
    void showError(@StringRes int res, View.OnClickListener errorClick);

    /**
     * 显示错误，并且提供点击回调
     */
    void showError(String res, View.OnClickListener errorClick);

}