package com.ftofs.twant.seller.fragment

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.ftofs.twant.BR
import com.ftofs.twant.R
import com.ftofs.twant.databinding.SellerEditFeaturesLayoutBinding
import com.ftofs.twant.entity.SellerGoodsItem
import com.ftofs.twant.kotlin.BaseTwantFragmentMVVM
import com.ftofs.twant.kotlin.FeatureGoodViewModel
import com.ftofs.twant.kotlin.SellerGoodsListAdapter
import com.ftofs.twant.log.SLog
import com.ftofs.twant.util.Util
import com.wzq.mvvmsmart.event.StateLiveData
import com.wzq.mvvmsmart.utils.KLog
import com.wzq.mvvmsmart.utils.ToastUtils

/**
 * 详情界面
 */
class SelectFeatureGoodsFragment : BaseTwantFragmentMVVM<SellerEditFeaturesLayoutBinding, FeatureGoodViewModel>(){
    private lateinit var sellerGoodsListAdapter: SellerGoodsListAdapter
    private val loadPopup by lazy {
        Util.createLoadingPopup(context)
    }

    companion object{
        fun newInstance(): SelectFeatureGoodsFragment {
            val args = Bundle()
            val fragment = SelectFeatureGoodsFragment()
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

    override fun initContentView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): Int {
        return R.layout.seller_edit_features_layout
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initData() {
        binding.btnBack.setOnClickListener{
            ToastUtils.showShort("點擊返回")
            hideSoftInputPop()
        }
        binding.btnMenu.setOnClickListener {
            ToastUtils.showShort("點擊菜單")
        }
        binding.iconAddGoods.visibility=View.GONE
        binding.tvTitle.text="選擇鎮店之寶"
        loadPopup.show()
        viewModel.doGetFeaturesGoodsList() //请求出售中的商品列表
        initRecyclerView()
    }

    override fun onBackPressedSupport(): Boolean {
        hideSoftInputPop()
        return false
    }

    private fun initRecyclerView() {
        sellerGoodsListAdapter = SellerGoodsListAdapter()
        binding.layoutManager = LinearLayoutManager(activity)
        binding.adapter = sellerGoodsListAdapter
    }
    override fun initViewObservable() {
        super.initViewObservable()
        viewModel.liveData.observe(this, Observer { goodsList: List<SellerGoodsItem> ->
            if (goodsList.isNotEmpty()) {
                SLog.info("mLiveData的listBeans.size():" + goodsList.size)
//                setBeautifulGirlImg(goodsList);  // 图片链接经常失效,设置美女图片,但每次上下拉头像会变;
            }
            if (viewModel.pageNum == 1) {
                sellerGoodsListAdapter.clear()
//                sellerGoodsListAdapter.data.clear() // 请求多页数据后再请求第1页,先删除之前数据
                if (goodsList.isEmpty()) {
                    //  第一页无数据,就显示默认页
//                    showEmptyLayout(binding.refreshLayout, this@SelectFeatureGoodsFragment.resources.getString(R.string.no_data_hint), R.drawable.ic_placeholder_no_data, false)
                } else {
//                    showNormalLayout(binding.refreshLayout)
                    sellerGoodsListAdapter.addAll(goodsList,true)
                }
            } else { // 不是第一页
                if (goodsList.isEmpty()) {
                    ToastUtils.showLong("开发者--本页无数据")
                    binding.refreshLayout.finishLoadMoreWithNoMoreData()
                    binding.refreshLayout.setNoMoreData(true)
                } else {
                    sellerGoodsListAdapter.addAll(goodsList,false)
                }
            }
        })
        binding.refreshLayout.setOnRefreshListener {
            viewModel.pageNum = 1
            viewModel.doGetFeaturesGoodsList()
        }
        //上拉加载更多
        binding.refreshLayout.setOnLoadMoreListener {
            viewModel.pageNum++
            //            loadMoreTestData();   // 模拟加载更多数据
            viewModel.doGetFeaturesGoodsList()
        }
        /**
         * 每个界面默认页效果不同
         * 在这里可以动态替换 无网络页,数据错误页, 无数据默认页;
         */
        viewModel.stateLiveData.stateEnumMutableLiveData.observe(this, Observer {
            when (it) {
                StateLiveData.StateEnum.Loading -> {
                    binding.refreshLayout.finishRefresh()
                    binding.refreshLayout.finishLoadMore()
                    loadPopup.show()
                    KLog.e("请求数据中--显示loading")
                }
                StateLiveData.StateEnum.Success -> {
                    binding.refreshLayout.finishRefresh()
                    binding.refreshLayout.finishLoadMore()
                    KLog.e("数据获取成功--关闭loading")
                }
                StateLiveData.StateEnum.Idle -> {
                    KLog.e("空闲状态--关闭loading")
                    binding.refreshLayout.finishRefresh()
                    binding.refreshLayout.finishLoadMore()
                    loadPopup.dismiss()
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
                    loadPopup.dismiss()
                }
            }
        })
    }
    // 无数据默认页,点击请求网络
    override fun onContentReload() {
        super.onContentReload()
        KLog.e("点击空白页")
        viewModel.doGetFeaturesGoodsList() //请求网络数据
        hideSoftInputPop()
    }



    override fun onDestroy() {
        super.onDestroy()
        loadPopup.dismiss()
    }
}