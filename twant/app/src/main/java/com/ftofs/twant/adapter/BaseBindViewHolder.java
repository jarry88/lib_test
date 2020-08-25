package com.ftofs.twant.adapter;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.ViewDataBinding;

import com.chad.library.adapter.base.BaseViewHolder;


public class BaseBindViewHolder<T extends ViewDataBinding>extends BaseViewHolder {
    protected final T binding;
    public BaseBindViewHolder(@NonNull T t) {
        super(t.getRoot());
        this.binding = t;
    }

    public T getBinding() {
        return binding;
    }
}
