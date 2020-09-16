package com.ftofs.twant.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.lifecycle.Observer
import cn.snailpad.easyjson.EasyJSONObject
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.ftofs.twant.BR
import com.ftofs.twant.R
import com.ftofs.twant.constant.PopupType
import com.ftofs.twant.databinding.FragmentImGoodsPageBinding
import com.ftofs.twant.databinding.ImGoodsListItemBinding
import com.ftofs.lib_net.model.StoreLabel
import com.ftofs.lib_net.model.Goods
import com.gzp.lib_common.base.BaseTwantFragmentMVVM
import com.ftofs.twant.kotlin.adapter.DataBoundAdapter
import com.ftofs.twant.kotlin.ui.ImGoodsSearch.ImGoodsEnum
import com.gzp.lib_common.utils.SLog
import com.ftofs.twant.util.ToastUtil
import com.ftofs.twant.viewmodel.ImGoodsPageModel
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.wzq.mvvmsmart.event.StateLiveData
import com.wzq.mvvmsmart.utils.KLog

class ImGoodsListPage(val type: ImGoodsEnum, val parent :ImGoodsFragment) : BaseTwantFragmentMVVM<FragmentImGoodsPageBinding, ImGoodsPageModel>(){

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
                binding.root.setOnClickListener {
                    parent.sendGoods?.onSelected(PopupType.IM_CHAT_SEND_GOODS,0,EasyJSONObject.generate(
                            "goodsName", item.goodsName,
                            "commonId", item.commonId,
                            "goodsImage", item.goodsImage
                    ))
                    parent.hideSoftInputPop()
                }

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
                                val absoluteAdapterPosition =adapterPosition
                                if(item.isFold==1){
                                    collapse(absoluteAdapterPosition)
                                }else{
                                    oldCategoryIndex=absoluteAdapterPosition
                                    expand(absoluteAdapterPosition)
                                }
                            }.apply {
                                setOnClickListener{
                                    val absoluteAdapterPosition =adapterPosition

                                    if (oldCategoryIndex != absoluteAdapterPosition) {
                                        item.apply {
                                            isFold=1-isFold
                                            viewModel.selectLabelId.postValue(item.storeLabelId)
                                        }
                                        notifyItemChanged(absoluteAdapterPosition)
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
                binding.btnClearAll.setOnClickListener { binding.etKeyword.text?.clear() }
//                binding.rvStoreLabel.visibility=View.VISIBLE
            }
            else -> SLog.info(type.searchType)
        }

        binding.rvGoodsList.adapter =goodsAdapter
        goodsAdapter.showEmptyView(true)
        binding.rvStoreLabel.adapter=labelAdapter

        binding.refreshLayout.setOnRefreshListener {
            viewModel.getImGoodsSearch()
        }
        binding.refreshLayout.setOnLoadMoreListener {
            viewModel.apply {
                getImGoodsSearch(selectLabelId.value?.toString(),keyword.value,false)
            }
        }
        binding.etKeyword.apply {
            setOnEditorActionListener { _, actionId, _ ->
                if ((actionId == EditorInfo.IME_ACTION_SEARCH) or (actionId == EditorInfo.IME_ACTION_UNSPECIFIED) or (0 == KeyEvent.KEYCODE_ENTER )) {
                    val search =text?.toString()?.trim()
                    if (notEmptyString(search)) viewModel.keyword.postValue(search)
                    else ToastUtil.success(context,"請輸入" + getString(R.string.input_order_search_hint))
                }
                return@setOnEditorActionListener true

            }
        }
    }

    private fun notEmptyString(text: String?): Boolean {
        text?.run { return length>0 }
                ?:return false
    }

    override fun initViewObservable() {
//        viewModel.showSearch.observe(this, Observer {if (it) binding.toolBar.visibility =})
        viewModel.searchType.observe(this, Observer { viewModel.getImGoodsSearch() })
//        viewModel.searchType

        viewModel.selectLabelId.observe(this, Observer { viewModel.getImGoodsSearch(labelId = it.toString()) })
        viewModel.keyword.observe(this, Observer { viewModel.apply {
            getImGoodsSearch(keyword =it)
        } })
        viewModel.goodsList.observe(this, Observer {
            if (!it.isNullOrEmpty()) {
                goodsAdapter.addAll(it,viewModel.isRefresh)
            }
            })
        viewModel.storeLabelList.observe(this, Observer {
            it.apply {
                if (isNotEmpty()) {
                    labelAdapter.setNewData(this)
                }
            }
             })

        viewModel.stateLiveData.stateEnumMutableLiveData.observe(this, Observer {
            when (it) {
                StateLiveData.StateEnum.Loading -> {
                    binding.refreshLayout.idle()
                    KLog.e("请求数据中--显示loading")
                }
                StateLiveData.StateEnum.Error -> {
                    binding.refreshLayout.finishRefresh()
                    binding.refreshLayout.finishLoadMore()
                    ToastUtil.error(context,viewModel.errorMessage)
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
                }
            }
        })
    }

    override fun onSupportVisible() {
        super.onSupportVisible()
        parent.targetName?.let {
            viewModel.targetName.value=it
            viewModel.searchType.postValue(type.searchType)
        }
    }
}

private fun SmartRefreshLayout.idle() {
    finishRefresh()
    finishLoadMore()
}

