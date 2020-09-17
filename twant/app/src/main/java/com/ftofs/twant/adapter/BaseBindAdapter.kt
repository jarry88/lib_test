package com.ftofs.twant.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.chad.library.adapter.base.BaseQuickAdapter
import com.gzp.lib_common.utils.SLog

abstract class BaseBindAdapter<T, D : ViewDataBinding>(val layoutResId: Int, data: List<T>) : BaseQuickAdapter<T, BoundViewHolder<D>>(layoutResId, data) {

    private val childList by lazy { mutableListOf<Int>() }
    fun setChildListRes(vararg int :Int){
        int.forEach {childList.add(it)}
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):BoundViewHolder<D> {
        //        onCreateViewHolder(viewHolder,viewType);
        SLog.info(layoutResId.toString())
        mContext=parent.context
        mLayoutInflater= LayoutInflater.from(mContext)
        val holder=BoundViewHolder<D>(DataBindingUtil.inflate(LayoutInflater.from(parent.context), layoutResId, parent, false))
        when (viewType) {
            else->{
//                bindViewClickListener(onCreateDefViewHolder(parent,viewType)
                bindClickListener(holder)
            }
        }

        return holder
    }


    private fun bindClickListener(holder: BoundViewHolder<D>) {
        onItemClickListener?.let {a->
            holder.itemView.isClickable = false
            holder.itemView
            holder.itemView.setOnClickListener{a.onItemClick(null,it,holder.adapterPosition) }
//            holder.itemView.isClickable=true

        }
        childList?.forEach{
            holder.binding.root.findViewById<View>(it).setOnClickListener { onItemChildClickListener?.onItemChildClick(null,it,holder.adapterPosition) }
        }
        SLog.info("完成點擊初始化")
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