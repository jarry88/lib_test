package com.ftofs.twant.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ftofs.twant.BR;

import java.util.List;

abstract public class BaseBindAdapter<T, D extends ViewDataBinding> extends BaseQuickAdapter<T,BaseBindViewHolder<D>> {
    public BaseBindAdapter(int layoutResId, @Nullable List<T> data) {
        super(layoutResId, data);
    }

    @NonNull
    @Override
    public BaseBindViewHolder<D> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        //        onCreateViewHolder(viewHolder,viewType);
        return new BaseBindViewHolder<D>((D) DataBindingUtil.inflate(inflater, mLayoutResId, parent, false));
    }

    @Override
    public void onBindViewHolder(BaseBindViewHolder<D> holder, int position) {
//        holder.getBinding().setVariable(BR.data, mDatas.get(position));
//        holder.getBinding().setVariable(BR.itemPresenter, ItemPresenter);
        holder.getBinding().executePendingBindings();
    }
    @Override
    public int getItemCount() {
        return getData() == null ? 0 : getData().size();
    }
}
