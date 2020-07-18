package com.ftofs.twant.fragment

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.ftofs.twant.BR
import com.ftofs.twant.R
import com.ftofs.twant.adapter.ShoppingStoreListAdapter
import com.ftofs.twant.databinding.SimpleRvListBinding
import com.ftofs.twant.entity.StoreItem
import com.ftofs.twant.kotlin.BaseTwantFragmentMVVM
import com.ftofs.twant.kotlin.LinkageShoppingListModel
import com.ftofs.twant.log.SLog
import com.ftofs.twant.util.UiUtil
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.wzq.mvvmsmart.event.StateLiveData
import com.wzq.mvvmsmart.utils.KLog
import com.wzq.mvvmsmart.utils.LoadingUtil

class LinkageShoppingListFragment :BaseTwantFragmentMVVM<SimpleRvListBinding, LinkageShoppingListModel>(){

    //    private var parent by lazy { arguments?.get("parent") }
    private lateinit var mAdapter: ShoppingStoreListAdapter
    private var loadingUtil: LoadingUtil? = null
    private val  zoneId by  lazy { arguments?.getInt("zoneId") }
    companion object{
        fun newInstance(zoneId:Int): LinkageShoppingListFragment {
            val args = Bundle()
            val fragment = LinkageShoppingListFragment()
            args.putInt("zoneId",zoneId)
//            args.put("parent",parent)
            fragment.arguments = args
            return fragment
        }
    }
    @SuppressLint("SourceLockedOrientationActivity")
    override fun initParam() {
        //获取列表传入的实体
        super.initParam()
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

    }
    fun getViewModel():LinkageShoppingListModel{
        return viewModel
    }
    override fun initContentView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): Int {
        return R.layout.simple_rv_list
    }

    override fun initVariableId(): Int {
       return BR.viewModel
    }

    override fun initData() {
//        ToastUtils.showShort(parentFragment.getText(R.id.tvTitle))
        initRecyclerView()
        zoneId?.let { viewModel.doGetStoreItems(it) } //请求网络数据
    }

    private fun initRecyclerView() {
        mAdapter = ShoppingStoreListAdapter(R.layout.store_view, arrayListOf<StoreItem>())
        binding.adapter = mAdapter


        parent.let {
            binding.rvSimple.isNestedScrollingEnabled=false
            binding.rvSimple.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    SLog.info("子頁面滾動監聽")
                    when (newState) {
                        RecyclerView.SCROLL_STATE_DRAGGING -> parent.onCbStartNestedScroll()
                        RecyclerView.SCROLL_STATE_IDLE->parent.onCbStopNestedScroll()
                    }
                }
            })

        }
    }
    override fun initViewObservable() {
        super.initViewObservable()

        viewModel.nestedScrollingEnable.observe(this, Observer {
            binding.rvSimple.isNestedScrollingEnabled=it
        })



        viewModel.storesList.observe(this, Observer { storesList:List<StoreItem>->
            if (storesList.isEmpty()) {
                SLog.info("空")
                return@Observer
            }
            mAdapter.setNewData(storesList)
        })
        binding.refreshLayout.setOnRefreshListener {
            viewModel.pageNum = 1
            zoneId?.let {
                viewModel.doGetStoreItems(it)
            }?:binding.refreshLayout.finishRefresh()
            binding.rvSimple.scrollToPosition(0)

        }
        //上拉加载更多
        binding.refreshLayout.setOnLoadMoreListener{ refreshLayout: RefreshLayout? ->
            //            loadMoreTestData();   // 模拟加载更多数据
            if (viewModel.hasMore) {
                viewModel.pageNum++//請求到數據后，如果獲得list為空，會在vm中將pagenum-1
                zoneId?.let { viewModel.doGetStoreItems(it) }
            } else {
                viewModel.stateLiveData.postNoMoreData()
            }
        }
        //備用參考上拉加載聯動功能
        /**
         * 每个界面默认页效果不同
         * 在这里可以动态替换 无网络页,数据错误页, 无数据默认页;
         */
        viewModel.stateLiveData.stateEnumMutableLiveData.observe(this, Observer {
            when (it) {
                StateLiveData.StateEnum.Loading -> {
                    binding.refreshLayout.finishRefresh()
                    binding.refreshLayout.finishLoadMore()
                    loadingUtil?.showLoading("加载中..")
                    KLog.e("请求数据中--显示loading")
                }
                StateLiveData.StateEnum.Success -> {
                    binding.refreshLayout.finishRefresh()
                    binding.refreshLayout.finishLoadMore()
                    KLog.e("数据获取成功--关闭loading")
                }
                StateLiveData.StateEnum.Idle -> {
                    KLog.e("空闲状态--关闭loading")
//                    binding.refreshLayout.finishRefresh()
//                    binding.refreshLayout.finishLoadMore()
                    loadingUtil?.hideLoading()
                }
                StateLiveData.StateEnum.NoData -> {
                    KLog.e("空闲状态--关闭loading")
                    binding.refreshLayout.finishRefresh()
                    binding.refreshLayout.finishLoadMore()
                }
                else -> {
                    KLog.e("其他状态--关闭loading")
                    binding.refreshLayout.finishRefresh()
                    binding.refreshLayout.finishLoadMore()
                    loadingUtil?.hideLoading()
                }
            }
        })
    }
    // 无数据默认页,点击请求网络
    override fun onContentReload() {
        super.onContentReload()
        KLog.e("点击空白页")
        zoneId?.let { viewModel.doGetStoreItems(it) } //请求网络数据
        hideSoftInputPop()
    }

    private fun test() {
//        val preSelectionIndex =mCategoryAdapter.prevSelectedItemIndex
//
//        if (mCategoryAdapter.hasNextSubItem(true)) {
//                    binding.rvZoneCategory.get(preSelectionIndex).findViewById<LinearLayout>(R.id.ll_sub_ategory_list).getChildAt(mCategoryAdapter.prevSelectedSubItemIndex + 1).performClick()
//                    viewModel.stateLiveData.postNoMoreData()
//
//                } else {
//                    if (preSelectionIndex +1 >= mCategoryAdapter.data.size) {
//                        viewModel.stateLiveData.postNoMoreData()
//                    } else {
//                        ToastUtils.showShort("點了$preSelectionIndex+1")
//                        binding.refreshLayout.finishLoadMore()
//                        viewModel.delayClick(preSelectionIndex+1,0)
//                    }
//                }
    }



    override fun onDestroy() {
        super.onDestroy()
        loadingUtil?.hideLoading()
    }

    fun scrollToTop() {
        UiUtil.moveToMiddle(binding.rvSimple,0)
    }
}