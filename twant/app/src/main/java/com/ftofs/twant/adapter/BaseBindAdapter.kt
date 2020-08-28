package com.ftofs.twant.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.ftofs.twant.log.SLog

abstract class BaseBindAdapter<T, D : ViewDataBinding>(val layoutResId: Int, data: List<T>) : BaseQuickAdapter<T, BoundViewHolder<D>>(layoutResId, data) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):BoundViewHolder<D> {
        //        onCreateViewHolder(viewHolder,viewType);
        SLog.info(layoutResId.toString())
        mContext=parent.context
        mLayoutInflater= LayoutInflater.from(mContext)
        val holder=BoundViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), layoutResId, parent, false) as D)
        when (viewType) {
            else->{
//                bindViewClickListener(onCreateDefViewHolder(parent,viewType)
                bindClickListener(holder)
            }
        }

        return holder
    }

    private fun bindClickListener(holder: BoundViewHolder<D>) {
        holder.let {
            onItemClickListener?.let {
                holder.itemView.setOnClickListener{
                    var position=holder.absoluteAdapterPosition
                    if(position==RecyclerView.NO_POSITION){
                        return@setOnClickListener
                    }
                    position -=headerLayoutCount
                    setOnItemClick(it,position)
                }
            }
        }
    }

    private fun bindViewClickListener(baseViewHolder: BaseViewHolder?) {
        baseViewHolder?.let {
            onItemClickListener?.let {
                baseViewHolder.itemView.setOnClickListener{
                    var position = baseViewHolder.adapterPosition
                    if (position == RecyclerView.NO_POSITION) {
                        return@setOnClickListener
                    }
                    position -= headerLayoutCount
                    setOnItemClick(it, position)
                }
            }
        }
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