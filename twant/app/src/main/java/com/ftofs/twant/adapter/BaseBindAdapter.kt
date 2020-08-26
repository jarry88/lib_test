package com.ftofs.twant.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.ftofs.twant.kotlin.adapter.DataBoundAdapter
import com.ftofs.twant.log.SLog

abstract class BaseBindAdapter<T, D : ViewDataBinding>(val layoutResId: Int, data: List<T>) : BaseQuickAdapter<T, BoundViewHolder<D>>(layoutResId, data) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BoundViewHolder<D> {
        //        onCreateViewHolder(viewHolder,viewType);
        SLog.info(layoutResId.toString())
        return BoundViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), layoutResId, parent, false) as D)
    }

    /**
     * 可以直接重寫initview 而不寫convert方法
     */
    override fun convert(helper: BoundViewHolder<D>, item: T) {
        initView(helper.binding,item)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    open fun initView(binding: D, item: T){

    }
}