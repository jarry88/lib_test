package com.ftofs.twant.kotlin.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.ftofs.twant.R
import com.ftofs.twant.interfaces.OnItemClickListener
import com.gzp.lib_common.utils.SLog
import java.util.zip.Inflater
import kotlin.reflect.KFunction
import kotlin.reflect.full.primaryConstructor

inline fun <reified T: View> new(vararg params: Any) =
        T::class.primaryConstructor?.call(params)
//        T::class.java.getDeclaredConstructor(*params.map { it::class.java }.toTypedArray()).apply { isAccessible = true }.newInstance(*params)
/**
 * @param c DslView 對象的context 含參主構造方法
 */
abstract class DslAdapter<T, V : View> (val c:KFunction<V>?,
        open val headId:Int=R.layout.category_head_item,
        private val footId:Int=R.layout.rl_foot_item,
        private val emptyId:Int=R.layout.ic_placeholder_no_data
) : RecyclerView.Adapter<DslViewHolder>() {

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
            DslViewHolder {
        context = parent.context
        return when (viewType) {
            emptyType -> DslViewHolder(View.inflate(context,emptyId,parent))
            footType -> DslViewHolder(View.inflate(context,footId,parent))
            headType -> DslViewHolder(View.inflate(context,headId,parent))
            else -> DslViewHolder(c?.call(context,null,0)?:View.inflate(context,emptyId,parent)).apply {
                SLog.info("a,${ c?.parameters?.size }")
            }
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
            holder: DslViewHolder,
            position: Int
    ) {
        if (isHeadPosition(position)) {
            initHeadView(holder.itemView)
        }else
        if (!isEmptyPosition(position)&&!isFootPosition(position)) {
            val realPosition=if(showHeadView)position-1 else position
            if (mData.isNotEmpty()&&realPosition<mData.size) {
                (holder.itemView as?V)?.let {
                    initView(it, mData[realPosition])
                }
            }
//            onItemClickListener?.let { holder.binding.root.apply {setOnClickListener { _->it.onClick(realPosition,this) }  } }

        }
//        holder.binding.executePendingBindings()//必须调用，否则闪屏

    }


    open fun initHeadView(headView: View) {}

    private fun isFootPosition(position: Int): Boolean {
        return   (position==itemCount-1)and showFootView
    }

    private fun isHeadPosition(position: Int): Boolean {
        return (position==0)and showHeadView
    }

    abstract fun initView(view: V, item: T)

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

    var onItemClickListener: OnItemClickListener?=null


}