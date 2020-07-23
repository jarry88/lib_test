package com.ftofs.twant.fragment

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.get
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.ftofs.twant.BR
import com.ftofs.twant.R
import com.ftofs.twant.adapter.ZoneCategoryListAdapter
import com.ftofs.twant.constant.Constant
import com.ftofs.twant.constant.PopupType
import com.ftofs.twant.databinding.LinkageContainerLayout2Binding
import com.ftofs.twant.entity.Goods
import com.ftofs.twant.interfaces.OnSelectedListener
import com.ftofs.twant.interfaces.SimpleCallback
import com.ftofs.twant.kotlin.*
import com.ftofs.twant.log.SLog
import com.ftofs.twant.tangram.NewShoppingSpecialFragment
import com.ftofs.twant.util.UiUtil
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.wzq.mvvmsmart.event.StateLiveData
import com.wzq.mvvmsmart.utils.KLog
import com.wzq.mvvmsmart.utils.LoadingUtil
import com.wzq.mvvmsmart.utils.ToastUtils
import java.util.*

class LinkageContainerFragment2 :BaseTwantFragmentMVVM<LinkageContainerLayout2Binding, LinkageContainerViewModel2>(){

    //    private var parent by lazy { arguments?.get("parent") }
    lateinit var parent:NewShoppingSpecialFragment

    private lateinit var mAdapter: BuyerGoodsListAdapter
    private lateinit var mCategoryAdapter: ZoneCategoryListAdapter
    private var loadingUtil: LoadingUtil? = null
    private val  zoneId by  lazy { arguments?.getInt("zoneId") }
    companion object{
        fun newInstance(zoneId:Int,p:NewShoppingSpecialFragment): LinkageContainerFragment2 {
            val args = Bundle()
            val fragment = LinkageContainerFragment2()
            args.putInt("zoneId",zoneId)
            args.putInt("zoneId",zoneId)
            fragment.parent = p

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
    fun getViewModel():LinkageContainerViewModel2{
        return viewModel
    }
    override fun initContentView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): Int {
        return R.layout.linkage_container_layout2
    }

    override fun initVariableId(): Int {
       return BR.viewModel
    }

    override fun initData() {
//        ToastUtils.showShort(parentFragment.getText(R.id.tvTitle))
        initRecyclerView()
        zoneId?.let { viewModel.doGetGoodsItems(it) } //请求网络数据
        zoneId?.let { viewModel.getZoneCategoryList(it) } //请求网络数据
    }

    private fun initRecyclerView() {
        mAdapter = BuyerGoodsListAdapter()
        binding.adapter = mAdapter


        mCategoryAdapter = ZoneCategoryListAdapter(context,R.layout.store_category_list_item,ArrayList<ZoneCategory>(), OnSelectedListener(fun (type:PopupType,id:Int,extra:Any){
            val subCategory = extra as ZoneCategory
//            ToastUtils.showShort("位置$id,name ${subCategory.categoryName},选中${subCategory.fold}")
            if (viewModel.currCategoryId.value != subCategory.categoryId) {
                viewModel.currCategoryId.value = subCategory.categoryId
                mCategoryAdapter.notifyItemChanged(id)
            } else {
                binding.rvRightList.scrollToPosition(0)
            }

        }))
        binding.categoryAdapter = mCategoryAdapter
        mCategoryAdapter.setOnItemClickListener { adapter, view, position ->
            val a =adapter.getItem(position) as ZoneCategory
            val prevSelectedItemIndex: Int = mCategoryAdapter.getPrevSelectedItemIndex()
//            ToastUtils.showShort("点击了${a.categoryName}前一个$prevSelectedItemIndex")
            if (prevSelectedItemIndex == position) {
                binding.rvRightList.scrollToPosition(0)
                val preIndex = mCategoryAdapter.prevSelectedSubItemIndex
                if (preIndex >= 0&&preIndex<a.nextList.size) {
                    a.nextList.get(preIndex).fold = Constant.FALSE_INT
                    mCategoryAdapter.prevSelectedSubItemIndex = -1
                    adapter.notifyItemChanged(position)
                }
            } else {
                //点击条目刷新后回到顶部
                UiUtil.moveToMiddle(binding.rvRightList,0)
                val prevSelectedItem = adapter.getItem(prevSelectedItemIndex) as ZoneCategory
                if (prevSelectedItemIndex != -1) {
                    prevSelectedItem.fold=Constant.FALSE_INT
                    adapter.notifyItemChanged(prevSelectedItemIndex)
                }
            }

            a.fold=Constant.TRUE_INT // 設置為展開狀態
            viewModel.currCategoryId.value=a.categoryId
            adapter.notifyItemChanged(position)

            mCategoryAdapter.prevSelectedItemIndex=position
        }

        parent?.let {
            binding.rvRightList.isNestedScrollingEnabled=false
            binding.rvRightList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    SLog.info("子頁面滾動監聽")
                    when (newState) {
                        RecyclerView.SCROLL_STATE_DRAGGING -> it.onCbStartNestedScroll()
                        RecyclerView.SCROLL_STATE_IDLE->it.onCbStopNestedScroll()
                    }
                }
            })

        }
    }
    override fun initViewObservable() {
        super.initViewObservable()

        viewModel.nestedScrollingEnable.observe(this, Observer {
            binding.rvRightList.isNestedScrollingEnabled=it
        })
        //检测当前选中categoryid变化
        viewModel.currCategoryId.observe(this, Observer {
            SLog.info("categoryid变化")
            binding.refreshLayout.autoRefresh()
        })
        //检测当前选中categoryIndex变化
        viewModel.currCategoryIndex.observe(this, Observer {
            if (mCategoryAdapter.data.isEmpty() || it >= mCategoryAdapter.data.size) {

            } else {
                binding.rvRightList.scrollToPosition(0)
                binding.rvZoneCategory.get(it).performClick()
            }
        })
        viewModel.categoryData.observe(this, Observer { categoryList:List<ZoneCategory>->
            if(categoryList.isEmpty()){
                SLog.info("categoryData.size为空")
                binding.rvZoneCategory.visibility= View.GONE
                return@Observer
            }

            mCategoryAdapter.setNewData(categoryList)

        })

        viewModel.goodsList.observe(this, Observer { goodsList:List<Goods>->
            if (viewModel.isRefresh) mAdapter.clear()
            mAdapter.addAll(goodsList,viewModel.isRefresh)
//
//            mAdapter.run {
//                if (viewModel.isRefresh) this.setnreplaceData(list.datas)
//                else addData(list.datas)
//            }
//            mAdapter.clear()
//            mAdapter.addAll(goodsList,viewModel.pageNum==1)
        })
        binding.refreshLayout.setOnRefreshListener {
            refresh()
        }



        //上拉加载更多
        binding.refreshLayout.setOnLoadMoreListener{ refreshLayout: RefreshLayout? ->
            //            loadMoreTestData();   // 模拟加载更多数据
            viewModel.isRefresh=false
            if (viewModel.hasMore) {
                viewModel.doGetZoneGoodsItems()
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
    private fun refresh() {
        viewModel.isRefresh=true
        viewModel.currCategoryId.value?.let{
            viewModel.doGetZoneGoodsItems()

        }?:binding.refreshLayout.finishRefresh()
        binding.rvRightList.scrollToPosition(0)
    }

    // 无数据默认页,点击请求网络
    override fun onContentReload() {
        super.onContentReload()
        KLog.e("点击空白页")
        viewModel.doGetZoneGoodsItems() //请求网络数据
        hideSoftInputPop()
    }

    private fun test() {
        val preSelectionIndex =mCategoryAdapter.prevSelectedItemIndex

        if (mCategoryAdapter.hasNextSubItem(true)) {
                    binding.rvZoneCategory.get(preSelectionIndex).findViewById<LinearLayout>(R.id.ll_sub_ategory_list).getChildAt(mCategoryAdapter.prevSelectedSubItemIndex + 1).performClick()
                    viewModel.stateLiveData.postNoMoreData()

                } else {
                    if (preSelectionIndex +1 >= mCategoryAdapter.data.size) {
                        viewModel.stateLiveData.postNoMoreData()
                    } else {
                        ToastUtils.showShort("點了$preSelectionIndex+1")
                        binding.refreshLayout.finishLoadMore()
                        viewModel.delayClick(preSelectionIndex+1,0)
                    }
                }
    }


    override fun onDestroy() {
        super.onDestroy()
        loadingUtil?.hideLoading()
    }

    fun scrollToTop() {
        binding.rvRightList.scrollToPosition(0)
    }
}