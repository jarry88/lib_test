package com.ftofs.twant.kotlin.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

/**
 * actor 晴天 create 2019/5/17
 * 封装一个kotlin下的通用adapter
 */

class KotlinBaseAdapter<T> private constructor() : RecyclerView.Adapter<KotlinBaseAdapter<T>.MyViewHolder>() {

    //数据
    private var mDatalist: ArrayList<T>? = null
    //布局id
    private var mLayoutId: Int? = null
    //绑定事件的lambda放发
    private var addBindView: ((itemView: View, itemData: T) -> Unit)? = null

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): MyViewHolder {
        val view = LayoutInflater.from(p0.context).inflate(mLayoutId!!, p0, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mDatalist?.size ?: -1 //左侧为null时返回-1
    }

    override fun onBindViewHolder(p0: MyViewHolder, p1: Int) {
        addBindView?.invoke(p0.itemView, mDatalist?.get(p1)!!)
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    /**
     * 建造者，用来完成adapter的数据组合
     */
    class Builder<B> {

        private var adapter: KotlinBaseAdapter<B> = KotlinBaseAdapter()

        /**
         * 设置数据
         */
        fun setData(lists: ArrayList<B>): Builder<B> {
            adapter.mDatalist = lists
            return this
        }

        /**
         * 设置布局id
         */
        fun setLayoutId(layoutId: Int): Builder<B> {
            adapter.mLayoutId = layoutId
            return this
        }

        /**
         * 绑定View和数据
         */
        fun addBindView(itemBind: ((itemView: View, itemData: B) -> Unit)): Builder<B> {
            adapter.addBindView = itemBind
            return this
        }

        fun create(): KotlinBaseAdapter<B> {
            return adapter
        }
    }

}
