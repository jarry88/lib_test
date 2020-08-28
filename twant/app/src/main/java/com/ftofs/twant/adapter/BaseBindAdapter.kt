package com.ftofs.twant.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.ftofs.twant.R
import com.ftofs.twant.log.SLog
import kotlinx.android.synthetic.main.sec_kill_list_normal_item.view.*

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
        childList?.forEach{
            holder.binding.root.findViewById<View>(it).setOnClickListener { onItemChildClickListener?.onItemChildClick(null,it,holder.absoluteAdapterPosition) }
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