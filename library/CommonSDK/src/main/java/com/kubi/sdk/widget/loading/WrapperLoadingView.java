package com.kubi.sdk.widget.loading;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

import com.kubi.sdk.R;

/**
 * @author <a href="emperor@kucoin.com">tql</a >
 * @version 2.0
 * @description
 * @time 2018/9/4
 */
@SuppressLint("ViewConstructor")
public class WrapperLoadingView extends FrameLayout implements ILoadingView {

    private static int LOADING_LAYOUT = R.layout.view_loading;
    private static int ERROR_LAYOUT = R.layout.view_fail;
    private static int EMPTY_LAYOUT = R.layout.view_empty;

    private View mContentView;
    private View mLoadingView;
    private View mErrorView;
    private View mEmptyView;

    /**
     * 可以注入任何view,但是fragment的容器会有问题，fragment manager里面保存了view的状态，直接修改之后会报view.unFocus(null); 空指针异常。
     *
     * @param targetView 必须有父类viewGroup
     * @return
     */
    public static WrapperLoadingView injectView(View targetView) {
        if (targetView.getParent() instanceof ViewGroup) {
            ViewGroup parent = (ViewGroup) targetView.getParent();
            int index = parent.indexOfChild(targetView);
            ViewGroup.LayoutParams layoutParams = targetView.getLayoutParams();
            parent.removeView(targetView);
            WrapperLoadingView mLoadingView = new WrapperLoadingView(targetView.getContext(), targetView);
            parent.addView(mLoadingView, index, layoutParams);
            return mLoadingView;
        }
        throw new RuntimeException("targetView must be child of viewGroup");
    }

    public WrapperLoadingView(@NonNull Context context, View contentView) {
        super(context);
        this.mContentView = contentView;
        LayoutInflater inflater = LayoutInflater.from(context);
        mLoadingView = inflater.inflate(LOADING_LAYOUT, null);
        mErrorView = inflater.inflate(ERROR_LAYOUT, null);
        mEmptyView = inflater.inflate(EMPTY_LAYOUT, null);
        addView(mLoadingView);
        addView(mErrorView);
        addView(mEmptyView);
        addView(mContentView, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        showContent();
    }

    @Override
    public void showLoading() {
        showLoading(Gravity.CENTER);
    }

    @Override
    public void showLoading(int gravity) {
        if (mLoadingView instanceof ViewGroup) {
            ((LinearLayout) mLoadingView).setGravity(gravity);
        }
        mContentView.setVisibility(GONE);
        mLoadingView.setVisibility(VISIBLE);
        mErrorView.setVisibility(GONE);
        mEmptyView.setVisibility(GONE);
    }

    @Override
    public void showContent() {
        mContentView.setVisibility(VISIBLE);
        mLoadingView.setVisibility(GONE);
        mErrorView.setVisibility(GONE);
        mEmptyView.setVisibility(GONE);
    }

    /**
     * 显示缺省布局
     *
     * @param res1            文字内容
     * @param res2            缺省图标
     * @param onClickListener 点击回调
     */
    @Override
    public void showEmpty(@StringRes int res1, @DrawableRes int res2, OnClickListener onClickListener) {
        mEmptyView.findViewById(R.id.empty_icon).setOnClickListener(onClickListener);
        mEmptyView.findViewById(R.id.empty_tips).setOnClickListener(onClickListener);
        TextView tips = mEmptyView.findViewById(R.id.empty_tips);
        if (res1 != 0) {
            tips.setText(getResources().getString(res1));
        } else {
            tips.setVisibility(INVISIBLE);
        }
        ImageView icon = mEmptyView.findViewById(R.id.empty_icon);
        if (res2 != 0) {
            icon.setImageResource(res2);
        } else {
            icon.setVisibility(INVISIBLE);
        }
        mContentView.setVisibility(GONE);
        mLoadingView.setVisibility(GONE);
        mErrorView.setVisibility(GONE);
        mEmptyView.setVisibility(VISIBLE);
    }

    @Override
    public void showError(@StringRes int res, OnClickListener errorClick) {
        if (res != 0) {
            showError(getResources().getString(res), errorClick);
        } else {
            showError(null, errorClick);
        }
    }

    @Override
    public void showError(String res, OnClickListener errorClick) {
        TextView tips = mErrorView.findViewById(R.id.fail_tips);
        if (!TextUtils.isEmpty(res)) {
            tips.setText(res);
        } else {
            tips.setVisibility(INVISIBLE);
        }

        mContentView.setVisibility(GONE);
        mLoadingView.setVisibility(GONE);
        mEmptyView.setVisibility(GONE);
        mErrorView.setVisibility(VISIBLE);
        mErrorView.setOnClickListener(errorClick);
    }

}
