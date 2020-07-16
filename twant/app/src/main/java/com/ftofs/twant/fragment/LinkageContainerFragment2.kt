package com.ftofs.twant.fragment

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.baozi.treerecyclerview.adpater.TreeRecyclerAdapter
import com.ftofs.twant.BR
import com.ftofs.twant.R
import com.ftofs.twant.databinding.LinkageContainerLayout2Binding
import com.ftofs.twant.entity.SellerGoodsItem
import com.ftofs.twant.kotlin.*
import com.ftofs.twant.log.SLog
import com.wzq.mvvmsmart.utils.KLog
import com.wzq.mvvmsmart.utils.LoadingUtil
import com.wzq.mvvmsmart.utils.ToastUtils
import java.util.ArrayList

class LinkageContainerFragment2 :BaseTwantFragmentMVVM<LinkageContainerLayout2Binding, LinkageContainerViewModel2>(){
    private lateinit var mAdapter: BuyerGoodsListAdapter
    private lateinit var mTreeAdapter: TreeRecyclerAdapter
    private var loadingUtil: LoadingUtil? = null
    private val  zoneId by  lazy { arguments?.getInt("zoneId") }
    companion object{
        fun newInstance(zoneId:Int): LinkageContainerFragment2 {
            val args = Bundle()
            val fragment = LinkageContainerFragment2()
            args.putInt("zoneId",zoneId)
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
        return R.layout.linkage_container_layout2
    }

    override fun initVariableId(): Int {
       return BR.viewModel
    }

    override fun initData() {
        binding.button2.setOnClickListener{
            ToastUtils.showShort("點擊返回")
            hideSoftInputPop()
        }

        initRecyclerView()
        zoneId?.let { viewModel.doGetGoodsItems(it) } //请求网络数据
    }
    private fun initRecyclerView() {
        mAdapter = BuyerGoodsListAdapter()
        binding.layoutManager = LinearLayoutManager(activity)
        binding.adapter = mAdapter

        mTreeAdapter = TreeRecyclerAdapter()
        binding.treeAdapter= mTreeAdapter
        binding.treeManager=LinearLayoutManager(activity)
    }
    override fun initViewObservable() {
        super.initViewObservable()
        /**
         * 每个界面默认页效果不同
         * 在这里可以动态替换 无网络页,数据错误页, 无数据默认页;
         */
        viewModel.stateLiveData.stateEnumMutableLiveData.observe(this, Observer {
            SLog.info(it.name)
        })
        viewModel.liveData.observe(this, Observer { goodsList: List<SellerGoodsItem> ->
            SLog.info("數據變動")
            if (goodsList.isNotEmpty()) {
                KLog.e("mLiveData的listBeans.size():" + goodsList.size)
                SLog.info("mLiveData的listBeans.size():" + goodsList.size)
//                setBeautifulGirlImg(goodsList);  // 图片链接经常失效,设置美女图片,但每次上下拉头像会变;
            }
            mAdapter.clear()
            mAdapter.addAll(goodsList,viewModel.pageNum==1)

        })
        viewModel.
    }
    // 无数据默认页,点击请求网络
    override fun onContentReload() {
        super.onContentReload()
        KLog.e("点击空白页")
//        viewModel.doGetServerNews() //请求网络数据
        hideSoftInputPop()
    }



    override fun onDestroy() {
        super.onDestroy()
        loadingUtil?.hideLoading()
    }
}