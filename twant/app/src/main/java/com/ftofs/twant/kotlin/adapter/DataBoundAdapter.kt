package com.ftofs.twant.kotlin.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.fastloan.app.ui.adapter.DataBoundViewHolder
import com.ftofs.twant.R

abstract class DataBoundAdapter<T, V : ViewDataBinding> :
    RecyclerView.Adapter<DataBoundViewHolder<V>>() {
    abstract val layoutId: Int
    private val emptyId= R.layout.ic_placeholder_no_data
    protected val mData = ArrayList<T>()
    lateinit var context:Context
    private val emptyType = 2
    private val itemType = 1
    private var showEmptyView =false


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            DataBoundViewHolder<V> {
        context = parent.context
        when (viewType) {
            emptyType ->return DataBoundViewHolder(
                    DataBindingUtil.inflate(
                            LayoutInflater.from(parent.context),
                            emptyId,
                            parent,
                            false
                    )
            )
            else -> return DataBoundViewHolder(
                    DataBindingUtil.inflate(
                            LayoutInflater.from(parent.context),
                            layoutId,
                            parent,
                            false
                    )
            )
        }

    }

    override fun getItemCount(): Int {
        val count = mData.size ?:0
        return if (count == 0 && showEmptyView) {
            1
        }else count
    }

    override fun onBindViewHolder(
        holder: DataBoundViewHolder<V>,
        position: Int
    ) {
        if (!isEmptyPosition(position)) {
            initView(holder.binding, mData[position])
        }
        holder.binding.executePendingBindings()//必须调用，否则闪屏

    }

    abstract fun initView(binding: V, item: T)

    fun addAll(list: List<T>, isFirst: Boolean) {
        if (isFirst) mData.clear()
        mData.addAll(list)
        notifyDataSetChanged()
    }

    fun getData(): List<T>? {
        return mData
    }
    /**
     * 判断是否是空布局
     */
    private fun isEmptyPosition( position:Int):Boolean {
        val count = getData()?.size?:0
        return (position == 0)and (count==0)and showEmptyView
    }
    fun showEmptyView(isShow :Boolean) {
        if (isShow != showEmptyView) {
            showEmptyView = isShow;
            notifyDataSetChanged();
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if(isEmptyPosition(position)) emptyType else itemType
    }
}