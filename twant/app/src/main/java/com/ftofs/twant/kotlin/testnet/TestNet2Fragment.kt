package com.ftofs.twant.kotlin.testnet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.ftofs.twant.BR
import com.ftofs.twant.R
import com.ftofs.twant.databinding.FragmentTestNe2Binding
import com.ftofs.twant.entity.SellerGoodsItem
import com.ftofs.twant.kotlin.BaseTwantFragmentMVVM
import com.ftofs.twant.kotlin.SellerGoodsListAdapter
import com.ftofs.twant.log.SLog
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.wzq.mvvmsmart.event.StateLiveData
import com.wzq.mvvmsmart.utils.KLog
import com.wzq.mvvmsmart.utils.LoadingUtil
import com.wzq.mvvmsmart.utils.ToastUtils

/**
 * Create Date：2019/01/25
 * Description：RecycleView多布局实现
 */
class TestNet2Fragment : BaseTwantFragmentMVVM<FragmentTestNe2Binding, TestNet2ViewModel>() {
    private lateinit var mAdapter: SellerGoodsListAdapter
    private var loadingUtil: LoadingUtil? = null

    private var count:Int = 0
    private val  zoneId by  lazy { arguments?.getInt("zoneId") }

    companion object{
        fun newInstance(zoneId:Int): TestNet2Fragment {
            val args = Bundle()
            val fragment = TestNet2Fragment()
            args.putInt("zoneId",zoneId)
            fragment.arguments = args
            return fragment
        }
    }
    override fun initContentView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): Int {
        return R.layout.fragment_test_ne2
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initData() {
        initRecyclerView()
    }

    override fun initViewObservable() {
        super.initViewObservable()
        binding.button.setOnClickListener { v: View? ->
            KLog.e("发起请求")
            viewModel.doPostServerNews() // 请求网络数据;
            viewModel.doGetServerNews()
        }
        binding.buttonScope.setOnClickListener { v: View? ->
            KLog.e("发起请求")
            viewModel.doScope() // 请求网络数据;
        }

        viewModel.liveData.observe(this, Observer {
            if (it.isNotEmpty()) {
                binding.tvJson.text = it[0].toString()
            }
        })








        viewModel.liveData.observe(this, Observer { goodsList: List<SellerGoodsItem> ->
            if (goodsList.isNotEmpty()) {
                KLog.e("mLiveData的listBeans.size():" + goodsList.size)
                SLog.info("mLiveData的listBeans.size():" + goodsList.size)
//                setBeautifulGirlImg(goodsList);  // 图片链接经常失效,设置美女图片,但每次上下拉头像会变;
            }
            if (viewModel.pageNum == 1) {
                mAdapter.clear()
//                sellerGoodsListAdapter.data.clear() // 请求多页数据后再请求第1页,先删除之前数据
                if (goodsList.isEmpty()) {
                    //  第一页无数据,就显示默认页
                    showEmptyLayout(binding.refreshLayout, this@TestNet2Fragment.resources.getString(R.string.tip_a_page_no_data), R.mipmap.ic_launcher_mvvmsmart, false)
                } else {
                    showNormalLayout(binding.refreshLayout)
                    mAdapter.addAll(goodsList,true)
                }
            } else { // 不是第一页
                if (goodsList.isEmpty()) {
                    ToastUtils.showLong("开发者--本页无数据")
                    binding.refreshLayout.finishLoadMoreWithNoMoreData()
                    binding.refreshLayout.setNoMoreData(true)
                } else {
                    mAdapter.addAll(goodsList,false)
                }
            }
        })
        binding.refreshLayout.setOnRefreshListener {
            viewModel.pageNum = 1
            viewModel.doGetServerNews()
//            zoneId?.let { it1 -> viewModel.doGetGoodsItems(it1) }
        }
        //上拉加载更多
        binding.refreshLayout.setOnLoadMoreListener { refreshLayout: RefreshLayout? ->
            viewModel.pageNum++
            //            loadMoreTestData();   // 模拟加载更多数据
//            zoneId?.let { viewModel.doGetGoodsItems(it) }
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
                    binding.refreshLayout.finishRefresh()
                    binding.refreshLayout.finishLoadMore()
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
    private fun initRecyclerView() {
        mAdapter = SellerGoodsListAdapter()
        binding.layoutManager = LinearLayoutManager(activity)
        binding.adapter = mAdapter
    }
    override fun onBackPressedSupport(): Boolean {
        hideSoftInputPop()
        return  true
    }

    override fun onSupportVisible() {
        super.onSupportVisible()
//        binding.btnToB.postDelayed({
//            count++
//            SLog.info("由A啓動B[%d]",count)
//            binding.btnToB.performClick()
//        }, 100)
    }
}