package com.ftofs.twant.fragment

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.baozi.treerecyclerview.adpater.TreeRecyclerAdapter
import com.baozi.treerecyclerview.base.ViewHolder
import com.baozi.treerecyclerview.factory.ItemHelperFactory
import com.ftofs.twant.BR
import com.ftofs.twant.R
import com.ftofs.twant.databinding.LinkageContainerLayout2Binding
import com.ftofs.twant.entity.Goods
import com.ftofs.twant.entity.SellerGoodsItem
import com.ftofs.twant.kotlin.*
import com.ftofs.twant.log.SLog
import com.ftofs.twant.util.UiUtil
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
            ToastUtils.showShort("點擊拉tree数据$zoneId")
            zoneId?.let { it1 -> viewModel.getZoneCategoryList(it1.toInt()) }
        }
        binding.btnGetData.setOnClickListener{
            ToastUtils.showShort("点击拉商品列表数据")
            viewModel.currCategoryId.value?.let { viewModel.doGetZoneGoodsItems(viewModel.currCategoryId.value?:20.toString(),viewModel.pageNum)}
        }

        initRecyclerView()
//        zoneId?.let { viewModel.doGetGoodsItems(it) } //请求网络数据
//        zoneId?.let { viewModel.getZoneCategoryList(it) } //请求网络数据
    }
    private fun initRecyclerView() {
        mAdapter = BuyerGoodsListAdapter()
        binding.layoutManager = LinearLayoutManager(activity)
        binding.adapter = mAdapter

        mTreeAdapter = TreeRecyclerAdapter()
        binding.treeAdapter= mTreeAdapter
        binding.treeManager=LinearLayoutManager(activity)
        mTreeAdapter.setOnItemClickListener(fun(v:ViewHolder?,p){
            v?.let {
                UiUtil.moveToMiddle(binding.recyclerView2,p)
                val groupItem=mTreeAdapter.getData(p) as ClickLoadGroupItem
                viewModel.currCategoryId.value=groupItem.getmDate().categoryId
            }?:UiUtil.moveToMiddle(binding.recyclerView2,p)
        })
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

        viewModel.categoryData.observe(this, Observer { categoryList:List<ZoneCategory>->
            if(categoryList.isEmpty()){
                SLog.info("categoryData.size为空")
                return@Observer
            }
            mTreeAdapter.clear()
            categoryList.forEach(fun (category:ZoneCategory){
                SLog.info(category.categoryName)
                mTreeAdapter.itemManager.addItem(ItemHelperFactory.createItem(category,ClickLoadGroupItem::class.java,null))

            })
        })
        viewModel.goodsList.observe(this, Observer { goodsList:List<Goods>->
            if (goodsList.isEmpty()) {
                SLog.info("空")
                return@Observer
            }
            mAdapter.clear()
            mAdapter.addAll(goodsList,viewModel.pageNum==1)
        })
    }
    // 无数据默认页,点击请求网络
    override fun onContentReload() {
        super.onContentReload()
        KLog.e("点击空白页")
//        viewModel.doGetServerNews() //请求网络数据
        hideSoftInputPop()
    }
    fun loadGoodsItems() {
        viewModel.doGetZoneGoodsItems(viewModel.currCategoryId.value?: 20.toString())
    }



    override fun onDestroy() {
        super.onDestroy()
        loadingUtil?.hideLoading()
    }
}