package com.ftofs.twant.kotlin.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.fastloan.app.ui.adapter.DataBoundViewHolder
import com.ftofs.twant.R
import com.ftofs.twant.log.SLog

abstract class DataBoundAdapter<T, V : ViewDataBinding> :
    RecyclerView.Adapter<DataBoundViewHolder<V>>() {
    abstract val layoutId: Int
    private val headId =R.layout.close_zone_top_item
    private val emptyId= R.layout.ic_placeholder_no_data
    private val footId= R.layout.rl_foot_item
    protected val mData = ArrayList<T>()
    lateinit var context:Context
    private val headType=4
    private val emptyType = 2
    private val footType = 3
    private val itemType = 1
    private var showEmptyView =false
    private var showFootView =false
    private var showHeadView =false




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
            footType ->return DataBoundViewHolder(
                    DataBindingUtil.inflate(
                            LayoutInflater.from(parent.context),
                            footId,
                            parent,
                            false
                    )
            )
            headType ->return DataBoundViewHolder(
                    DataBindingUtil.inflate(
                            LayoutInflater.from(parent.context),
                            headId,
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
        val count = mData.size
        return if (count == 0 && showEmptyView) {
            1
        }else if (showFootView && showHeadView) {
            count +2
        }else if (showFootView || showHeadView) {
            count + 1
        } else {
            count
        }
    }

    override fun onBindViewHolder(
        holder: DataBoundViewHolder<V>,
        position: Int
    ) {
        if (!isEmptyPosition(position)&&!isFootPosition(position)&&!isHeadPosition(position)) {
            initView(holder.binding, mData[if(showHeadView)position-1 else position])
        }
        holder.binding.executePendingBindings()//必须调用，否则闪屏

    }

    private fun isFootPosition(position: Int): Boolean {
        return   (position==itemCount-1)and showFootView
    }

    private fun isHeadPosition(position: Int): Boolean {
        val result = (position==0)and showHeadView
        SLog.info(result.toString() +"  $itemCount  $showHeadView" )
        return  result
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
    fun showFootView(isShow :Boolean) {
        if (isShow != showFootView) {
            showFootView = isShow;
            notifyDataSetChanged();
        }
    }
    fun showHeadView(isShow: Boolean) {
        if (isShow != showHeadView) {
            showHeadView = isShow
            notifyDataSetChanged()
        }
    }
    override fun getItemViewType(position: Int): Int {
        return if(isEmptyPosition(position)) emptyType else if(isFootPosition(position))footType else if(isHeadPosition(position)) headType else itemType
    }


}