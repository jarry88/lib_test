package com.ftofs.twant.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.ftofs.twant.R
import com.ftofs.twant.databinding.SmartListViewBinding
import com.ftofs.twant.dsl.customer.factoryAdapter
import com.gzp.lib_common.utils.SLog

class SmartListView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) ,LifecycleObserver{
    var onRefresh =true
    var hasMore =true
    var fragment:Fragment?=null
    val inflater =context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    val mBinding =DataBindingUtil.inflate<SmartListViewBinding>(inflater,R.layout.smart_list_view,this,true)
    init {

        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.SmartList)
//        typedArray.getString(R.styleable.Title_text_title)?.let {
//            text=it
//        }
//        //不設置的時候是顯示的
//        typedArray.getBoolean(R.styleable.Title_login_info, false).takeIf { it }?.let {
//            rootView.findViewById<View>(R.id.tv_info)?.visibility= View.VISIBLE
//        }
//        typedArray.getBoolean(R.attr.login_info,false).let {
//            if(it) rootView.findViewById<TextView>(R.id.tv_info)?.visibility= VISIBLE
//        }
        typedArray.recycle()

    }
    //如果控件依附于fragment则调用这个方法，传入fragment
    fun attachFragment(fragment: Fragment) {
        this.fragment = fragment
    }
    //这个方法用于返回LifecycleOwner
    private fun getLifecycleOwner(): LifecycleOwner? {
        if (fragment == null) {
            if (context is LifecycleOwner) {
                return context as LifecycleOwner
            }
        } else {
            if (fragment is LifecycleOwner) {
                return fragment
            }
        }
        return null
    }

    fun <T,V:ViewDataBinding>config(resId:Int,list:LiveData<List<T>?>,initAdapter:(V,T)->Unit){
        val adapter= factoryAdapter(resId,initAdapter).apply { showEmptyView(true) }
        mBinding.rvList.adapter=adapter
        getLifecycleOwner()?.let {
            list.observe(it){
                it?.let {
                    SLog.info("容器收货")
                    adapter.addAll(it,onRefresh)
                }?:adapter.addAll(listOf(),true).apply {                     SLog.info("空数据")
                }
                adapter.notifyDataSetChanged()
                endLoadingUi()
            }
        }

    }
    fun setLoadMoreListener(loadMore:()->Unit)=mBinding.refreshLayout.setOnLoadMoreListener {
        onRefresh=false
        loadMore() }
    fun setRefreshListener(refresh:()->Unit)=mBinding.refreshLayout.setOnRefreshListener {
        onRefresh=true
        refresh() }
    fun endLoadingUi()=mBinding.refreshLayout.apply {
        finishLoadMore()
        finishRefresh()
    }
    fun onLoading() =mBinding.refreshLayout.run {
        onRefresh
    }
    fun autoRefresh(){
        mBinding.refreshLayout.finishRefresh()
        mBinding.refreshLayout.autoRefresh()}
    fun setOrientation(){
//        mBinding.refreshLayout.isHorizontalFadingEdgeEnabled=true
        mBinding.rvList.isHorizontalFadingEdgeEnabled=true
        mBinding.rvList.layoutManager=LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
    }
}