package com.android.framework.jc.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.android.framework.jc.base.mvp.IFkContract;
import com.android.framework.jc.wrapper.IViewWrapper;

import java.util.ArrayList;

import javax.inject.Inject;

/**
 * @author Mr.Hu(Jc) JCFramework
 * @create 2018/7/16 18:28
 * @describe
 * @update
 */
public abstract class FkFragment<P extends IFkContract.IPresenter> extends Fragment implements IFkContract.IView {
    protected Context mContext;
    private final ArrayList<IViewWrapper> headWrappers = new ArrayList<>();
    private final ArrayList<IViewWrapper> footWrappers = new ArrayList<>();
    @Inject
    protected P mPresenter;

    @CallSuper
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onCreateComponent();
        if (mPresenter != null) {
            getLifecycle().addObserver(mPresenter);
        }
        for (IViewWrapper wrapper : headWrappers) {
            getLifecycle().addObserver(wrapper);
        }
        for (IViewWrapper wrapper : footWrappers) {
            getLifecycle().addObserver(wrapper);
        }
    }

    protected abstract void onCreateComponent();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = onCreateRootView(inflater, container, savedInstanceState);
        if (headWrappers.size() > 0 || footWrappers.size() > 0) {
            LinearLayout result;
            if (rootView instanceof LinearLayout && ((LinearLayout) rootView).getOrientation() == LinearLayout.VERTICAL) {
                result = (LinearLayout) rootView;
                addView(result, inflater, container, savedInstanceState);
            } else {
                result = new LinearLayout(mContext);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                result.setLayoutParams(params);
                result.setOrientation(LinearLayout.VERTICAL);
                result.addView(rootView);
                addView(result, inflater, container, savedInstanceState);
            }
            return result;
        }

        return rootView;
    }


    /**
     * 添加头部装饰者
     *
     * @param wrapper
     *         头部装饰者
     */
    public void addHeadWrapper(IViewWrapper wrapper) {
        headWrappers.add(wrapper);
    }

    /**
     * 添加尾部装饰者
     *
     * @param wrapper
     *         wrapper
     */
    public void addFootWrapper(IViewWrapper wrapper) {
        footWrappers.add(wrapper);
    }

    /**
     * 根据上层的view动态添加头部跟尾部装饰者View进去
     *
     * @param rootView
     *         rootView
     * @param inflater
     *         inflater
     * @param container
     *         container
     * @param savedInstanceState
     *         savedInstanceState
     */
    private void addView(LinearLayout rootView, LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        for (int i = 0; i < headWrappers.size(); i++) {
            View headView = headWrappers.get(i).onCreateView(inflater, null, savedInstanceState);
            if (headView == null) {
                continue;
            }
            if (headView.getParent() != null) {
                throw new IllegalArgumentException("传入头部装饰者的rootView必须没有父控件！");
            }
            rootView.addView(headView, i);

        }
        for (IViewWrapper wrapper : footWrappers) {
            View footView = wrapper.onCreateView(inflater, null, savedInstanceState);
            if (footView == null) {
                continue;
            }
            if (footView.getParent() != null) {
                throw new IllegalArgumentException("传入尾部装饰者的rootView必须没有父控件！");
            }
            rootView.addView(footView, rootView.getChildCount() - 1);
        }

    }

    protected void onFinish() {
        if (mContext instanceof Activity) {
            ((Activity) mContext).finish();
        }
    }

    /**
     * 关闭页面并回调
     */
    protected void onResultOkFinish() {
        onResultOkFinish(null);
    }

    /**
     * 关闭页面并回调
     */
    protected void onResultOkFinish(Intent intent) {
        if (mContext instanceof Activity) {
            ((Activity) mContext).setResult(Activity.RESULT_OK, intent);

            ((Activity) mContext).finish();
        }
    }

    /**
     * 如果当前fragment属于viewPager中的一个页面，viewPager选中的时候调用此方法
     */
    public void onViewPagerSelected() {

    }

    /**
     * 如果当前fragment属于viewPager中的一个页面，viewPager选中的时候调用此方法
     */
    public void onViewPagerUnSelected() {

    }

    /**
     * 上层添加布局View
     *
     * @param inflater
     *         inflater
     * @param container
     *         container
     * @param savedInstanceState
     *         savedInstanceState
     *
     * @return 上层添加布局View
     */
    protected abstract View onCreateRootView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState);


}
