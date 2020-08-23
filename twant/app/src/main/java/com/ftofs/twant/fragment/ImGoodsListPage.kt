package com.ftofs.twant.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.ftofs.twant.BR
import com.ftofs.twant.R
import com.ftofs.twant.databinding.FragmentImGoodsPageBinding
import com.ftofs.twant.databinding.ImGoodsListItemBinding
import com.ftofs.twant.databinding.ZoneGoodsListItemBindingImpl
import com.ftofs.twant.domain.store.StoreLabel
import com.ftofs.twant.entity.Goods
import com.ftofs.twant.entity.SellerGoodsItem
import com.ftofs.twant.kotlin.BaseTwantFragmentMVVM
import com.ftofs.twant.kotlin.LinkageShoppingListModel
import com.ftofs.twant.kotlin.adapter.DataBoundAdapter
import com.ftofs.twant.kotlin.ui.ImGoodsSearch.ImGoodsEnum
import com.ftofs.twant.log.SLog
import com.ftofs.twant.util.ToastUtil
import com.ftofs.twant.viewmodel.ImGoodsPageModel
import com.lyrebirdstudio.croppylib.util.extensions.visible
import com.wzq.mvvmsmart.event.StateLiveData
import com.wzq.mvvmsmart.utils.KLog
import com.wzq.mvvmsmart.utils.LoadingUtil

class ImGoodsListPage(val type: ImGoodsEnum) :BaseTwantFragmentMVVM<FragmentImGoodsPageBinding, ImGoodsPageModel>(){

    private var loadingUtil: LoadingUtil? = null
    private var oldCategoryIndex=0
    override fun initContentView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): Int {
        return R.layout.fragment_im_goods_page
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    private val goodsAdapter by lazy {
        object :DataBoundAdapter<Goods,ImGoodsListItemBinding>(){
            override val layoutId: Int
                get() = R.layout.im_goods_list_item

            override fun initView(binding: ImGoodsListItemBinding, item: Goods) {
                binding.vo=item
            }


        }
    }
    private val labelAdapter by lazy {
        object :BaseMultiItemQuickAdapter<StoreLabel,BaseViewHolder>(null ){
            private val ITEM_TYPE0 = 0 // 布局类型：一级列表
            private val ITEM_TYPE1 = 1 // 布局类型：二级列表
//            private var api: AdapterClickApi? = null // 回掉接口
            init {
                //有几个布局就加几个addItemType，一参事自定义的常量类型，二参是该列表的item布局
                addItemType(ITEM_TYPE0, R.layout.store_category_list_item)
//                addItemType(ITEM_TYPE1, R.layout.layout_item1)
            }

            override fun convert(helper: BaseViewHolder, item: StoreLabel?) {
                helper.getView<TextView>(R.id.tv_category_name).apply { if(textSize!= 14f) textSize=14f }

                when(item?.itemType){
                    ITEM_TYPE0 ->{
                        helper.apply {
                            setText(R.id.tv_category_name,item.storeLabelName)
                            setTextColor(R.id.tv_category_name,if(item.isFold==1) Color.BLACK else context?.resources?.getColor(R.color.blue)?:Color.BLUE)
                            setGone(R.id.vw_selected_indicator,item.isFold!=1)
                            itemView.apply {
                                if(item.isFold==1){
                                    collapse(adapterPosition)
                                }else{
                                    oldCategoryIndex=adapterPosition
                                    expand(adapterPosition)
                                }
                            }.apply {
                                setOnClickListener{
                                    if (oldCategoryIndex != adapterPosition) {
                                        item.apply { isFold=1-isFold }
                                        notifyItemChanged(adapterPosition)
                                        if(oldCategoryIndex in 0 ..data.size)
                                            data[oldCategoryIndex].apply {
                                                isFold=1-isFold
                                                notifyItemChanged(oldCategoryIndex)
                                            }


                                    }

                                }
                            }
                        }

                    }
                }
            }
        }
    }
    override fun initData() {
        SLog.info(type.searchType)

        binding.viewModel=viewModel
        when (type) {
            ImGoodsEnum.RECOMMEND ,ImGoodsEnum.OWNER->{
                binding.toolBar.visibility= View.VISIBLE
                binding.rvStoreLabel.visibility=View.VISIBLE
            }
            else -> SLog.info(type.searchType)
        }

        binding.rvGoodsList.adapter =goodsAdapter
        goodsAdapter.showEmptyView(true)
        binding.rvStoreLabel.adapter=labelAdapter
    }

    override fun initViewObservable() {
//        viewModel.showSearch.observe(this, Observer {if (it) binding.toolBar.visibility =})
        viewModel.searchType.observe(this, Observer { viewModel.getImGoodsSearch() })
        viewModel.goodsList.observe(this, Observer {
            goodsAdapter.addAll(it,viewModel.isRefresh) })
        viewModel.storeLabelList.observe(this, Observer {
            it.apply {
                if (isNotEmpty()) {
                    labelAdapter.setNewData(this)
                }
                binding.rvStoreLabel.VorG(isNotEmpty())
            }
             })

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

                    labelAdapter.emptyView
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

    override fun onSupportVisible() {
        super.onSupportVisible()
        viewModel.searchType.postValue(type.searchType)
    }
}

private fun View.VorG(bool: Boolean) {
    visibility=if(bool)View.VISIBLE else View.GONE
}
