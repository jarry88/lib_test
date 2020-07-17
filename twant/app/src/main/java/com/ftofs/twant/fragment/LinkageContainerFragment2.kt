package com.ftofs.twant.fragment

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.baozi.treerecyclerview.adpater.TreeRecyclerAdapter
import com.baozi.treerecyclerview.base.ViewHolder
import com.baozi.treerecyclerview.factory.ItemHelperFactory
import com.ftofs.twant.BR
import com.ftofs.twant.R
import com.ftofs.twant.adapter.ZoneCategoryListAdapter
import com.ftofs.twant.constant.Constant
import com.ftofs.twant.constant.PopupType
import com.ftofs.twant.databinding.LinkageContainerLayout2Binding
import com.ftofs.twant.domain.store.StoreLabel
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
import org.litepal.util.Const
import java.util.*

class LinkageContainerFragment2 :BaseTwantFragmentMVVM<LinkageContainerLayout2Binding, LinkageContainerViewModel2>(){
    private var parent by lazy { arguments?.get("parent") }
    private lateinit var mAdapter: BuyerGoodsListAdapter
    private lateinit var mTreeAdapter: TreeRecyclerAdapter
    private lateinit var mCategoryAdapter: ZoneCategoryListAdapter
    private var loadingUtil: LoadingUtil? = null
    private val  zoneId by  lazy { arguments?.getInt("zoneId") }
    companion object{
        fun newInstance(zoneId:Int,parent:NewShoppingSpecialFragment): LinkageContainerFragment2 {
            val args = Bundle()
            val fragment = LinkageContainerFragment2()
            args.putInt("zoneId",zoneId)
            args.put("parent",parent)
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
        ToastUtilsparentFragment::class.java.name
        initRecyclerView()
        zoneId?.let { viewModel.doGetGoodsItems(it) } //请求网络数据
        zoneId?.let { viewModel.getZoneCategoryList(it) } //请求网络数据
    }

    private fun initRecyclerView() {
        mAdapter = BuyerGoodsListAdapter()
        binding.adapter = mAdapter

        mTreeAdapter = TreeRecyclerAdapter()
        binding.treeAdapter= mTreeAdapter

        mCategoryAdapter = ZoneCategoryListAdapter(context,R.layout.store_category_list_item,ArrayList<ZoneCategory>(), OnSelectedListener(fun (type:PopupType,id:Int,extra:Any){
            ToastUtils.showShort("s")
        }))
        binding.categoryAdapter = mCategoryAdapter
        mCategoryAdapter.setOnItemClickListener { adapter, view, position ->
            val a =adapter.getItem(position) as ZoneCategory
            ToastUtils.showShort("点击了${a.categoryName}")
            val prevSelectedItemIndex: Int = mCategoryAdapter.getPrevSelectedItemIndex()
            if (prevSelectedItemIndex != -1) {
                val prevSelectedItem:ZoneCategory = viewModel.categoryData.value?.get(prevSelectedItemIndex)!!
                prevSelectedItem.fold=Constant.TRUE_INT
            }
            viewModel.categoryData.value?.get(position)?.fold=Constant.FALSE_INT // 設置為展開狀態
            viewModel.currCategoryId.value=viewModel.currCategoryId.value
            mCategoryAdapter.prevSelectedItemIndex=position

            viewModel.currCategoryId.value=a.categoryId
        }

        mTreeAdapter.setOnItemClickListener(fun(v:ViewHolder?,p){
            v?.let {
                UiUtil.moveToMiddle(binding.recyclerView2,p)
                context?.getColor(R.color.tw_blue)?.let { it1 -> it.getTextView(R.id.tv_category_name).setTextColor(it1) }
                if (mTreeAdapter.getData(p) is ClickLoadGroupItem) {
                    val groupItem=mTreeAdapter.getData(p) as ClickLoadGroupItem
                    viewModel.currCategoryId.value=groupItem.getmDate().categoryId
                    groupItem.isExpand = !groupItem.isExpand

//                    groupItem.clickChild.forEach(fun(item){
//                        item.selected=item.data.categoryId.equals(viewModel.currCategoryId.value)
//                    })
//                    mTreeAdapter.notifyDataSetChanged()
                }
            }?:UiUtil.moveToMiddle(binding.recyclerView2,p)
        })
    }
    override fun initViewObservable() {
        super.initViewObservable()

        viewModel.categoryData.observe(this, Observer { categoryList:List<ZoneCategory>->
            if(categoryList.isEmpty()){
                SLog.info("categoryData.size为空")
                return@Observer
            }
            mTreeAdapter.clear()
            val items= arrayListOf<ClickLoadGroupItem>()
            categoryList.forEach(fun (category:ZoneCategory){
                SLog.info(category.categoryName)
                val item =ItemHelperFactory.createItem(category,ClickLoadGroupItem::class.java,null) as ClickLoadGroupItem
                item.callback= SimpleCallback { data ->
                    data as ZoneCategory
                    ToastUtils.showShort(data.categoryName)
                    viewModel.currCategoryId.value=data.categoryId
                    SLog.info(data.categoryId)
                }
                item.mContext=context
                items.add(item)

            })
            mTreeAdapter.itemManager.replaceAllItem(items as List<ClickLoadGroupItem>?)
            mCategoryAdapter.setNewData(categoryList)
        })
        viewModel.currCategoryId.observe(this, Observer {
            binding.refreshLayout.autoRefresh()
        })
        viewModel.goodsList.observe(this, Observer { goodsList:List<Goods>->
            if (goodsList.isEmpty()) {
                SLog.info("空")
                return@Observer
            }
            mAdapter.clear()
            mAdapter.addAll(goodsList,viewModel.pageNum==1)
        })
        binding.refreshLayout.setOnRefreshListener {

            viewModel.pageNum = 1
            viewModel.currCategoryId.value?.let{
                viewModel.doGetZoneGoodsItems(it)

            }?:it.finishRefresh()
            binding.recyclerView2.scrollToPosition(0)

        }
        //上拉加载更多
        binding.refreshLayout.setOnLoadMoreListener{ refreshLayout: RefreshLayout? ->
            viewModel.pageNum++
            //            loadMoreTestData();   // 模拟加载更多数据
            viewModel.currCategoryId.value?.let{
                viewModel.doGetZoneGoodsItems(it)
            }?:refreshLayout?.finishLoadMore()
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