package com.android.framework.jc.recyclerview;

import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.StringRes;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * @author Mr.Hu(Jc) JCFramework
 * @create 2018/3/23 15:31
 * @describe
 * @update
 */

public class ViewHolder extends RecyclerView.ViewHolder {

    private final SparseArray<View> mViews;

    private ViewHolder(View itemView) {
        super(itemView);
        mViews = new SparseArray<>();
    }

    public static ViewHolder create(View itemView) {
        return new ViewHolder(itemView);
    }

    public static ViewHolder create(ViewGroup parentView, @LayoutRes int layoutId) {
        return new ViewHolder( LayoutInflater.from(parentView.getContext()).inflate(layoutId, parentView, false));
    }

    public View getItemView() {
        return itemView;
    }

    /**
     * 通过viewId获取控件
     *
     * @param viewId
     *         viewId
     *
     * @return View
     */
    public <T extends View> T getView(@IdRes int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = itemView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        //noinspection unchecked
        return (T) view;
    }

    /**
     * 为TextView 设置数据
     *
     * @param viewId
     *         viewId
     * @param value
     *         value
     *
     * @return ViewHolder
     */
    public ViewHolder setText(@IdRes int viewId, String value) {
        TextView textView = getView(viewId);
        textView.setText(value);
        return this;
    }

    /**
     * 为TextView 设置数据
     *
     * @param viewId
     *         viewId
     * @param valueId
     *         valueId
     *
     * @return ViewHolder
     */
    public ViewHolder setText(@IdRes int viewId, @StringRes int valueId) {
        TextView textView = getView(viewId);
        textView.setText(valueId);
        return this;
    }

    public ViewHolder setVisibility(@IdRes int viewId, int visibility) {
        getView(viewId).setVisibility(visibility);
        return this;
    }

}
