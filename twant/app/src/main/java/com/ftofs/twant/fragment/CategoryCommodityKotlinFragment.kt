package com.ftofs.twant.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.Observer
import cn.snailpad.easyjson.EasyJSONObject
import com.ftofs.twant.BR
import com.ftofs.twant.R
import com.ftofs.twant.adapter.CategoryGridAdapter
import com.ftofs.twant.constant.SearchType
import com.ftofs.twant.databinding.CategoryCommodityBrandMenuItemBinding
import com.ftofs.twant.databinding.CategoryCommodityTableItemBinding
import com.ftofs.twant.databinding.PageCategoryCommodityBinding
import com.ftofs.twant.kotlin.BaseTwantFragmentMVVM
import com.ftofs.twant.kotlin.CategoryCommodityViewModel
import com.ftofs.twant.kotlin.adapter.DataBoundAdapter
import com.ftofs.twant.log.SLog
import com.ftofs.twant.util.Util
import com.ftofs.twant.vo.CategoryNavVo
import com.lxj.xpopup.core.BasePopupView
import com.wzq.mvvmsmart.event.StateLiveData
import com.wzq.mvvmsmart.utils.KLog
import com.wzq.mvvmsmart.utils.LoadingUtil

//
//private val Int.toPx: Int
//    get() {return U}

class CategoryCommodityKotlinFragment:BaseTwantFragmentMVVM<PageCategoryCommodityBinding, CategoryCommodityViewModel>() {
    private var loadingUtil:LoadingUtil?=null
    private val loadPopup by lazy {
        Util.createLoadingPopup(context)
    }

    override fun initContentView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): Int {
        return  R.layout.page_category_commodity
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }
    private val menuAdapter by lazy { object :DataBoundAdapter<CategoryNavVo,CategoryCommodityBrandMenuItemBinding>(){
        var oldPosition=0
        var currPosition=0
        var fold=1
        override val layoutId: Int
            get() = R.layout.category_commodity_brand_menu_item

        override fun initView(binding: CategoryCommodityBrandMenuItemBinding, item: CategoryNavVo) {
            binding.vo=item
            binding.root.setOnClickListener {

                if (expandMenu(fold)) {
                    fold = 0
                    return@setOnClickListener
                } else {
                    fold =1
                    item.isFold=0
                    getData()?.indexOf(item)?.let {
                        currPosition=it
                        val oldItem=getData()?.get(oldPosition)
                        if (oldPosition == currPosition&&oldItem?.isFold==0) {
                            return@setOnClickListener
                        }
                        oldItem?.isFold=1
                        notifyItemChanged(it)
                        notifyItemChanged(oldPosition)
                        oldPosition=currPosition
                    }
//                    getData()?.forEachIndexed { index, categoryNavVo -> if(categoryNavVo.isFold==0&&index==oldPosition) }
                }

            }
            if (item.isFold == 0) {
                item.categoryList?.let { tableAdapter.addAll(it,true)
                    SLog.info(it.size.toString())
                }
            }
        }

    } }

    private fun expandMenu(fold: Int): Boolean {
        val result= fold==1
//        val params =binding.rvCategoryMenu.layoutParams
//        if (result) {
//            params.width = LinearLayout.LayoutParams.WRAP_CONTENT
//        } else {
//            params.width=Util.dip2px(context,100f)
//
//        }
//        binding.rvCategoryMenu.layoutParams=params
        binding.rvCategoryMenu.apply {
            layoutParams=layoutParams.apply {
                width=if(result) (Util.getScreenDimension(_mActivity).first*0.67).toInt() else Util.dip2px(context,100f)
            }
            menuAdapter.notifyDataSetChanged()
//            tableAdapter.notifyDataSetChanged()
        }
        return result
    }

    private val tableAdapter by lazy { object :DataBoundAdapter<CategoryNavVo,CategoryCommodityTableItemBinding>(){
        override val layoutId: Int
            get() = R.layout.category_commodity_table_item

        override fun initView(binding: CategoryCommodityTableItemBinding, item: CategoryNavVo) {
            item.run {
                binding.vo=this
            }
            binding.tvTitle.setOnClickListener {
                Util.startFragment(SearchResultFragment.newInstance(SearchType.GOODS.name,
                        EasyJSONObject.generate("cat", item.categoryId.toString()).toString()))
            }
            item.categoryList.size.takeIf { it>0 }.let { binding.adapter= CategoryGridAdapter().apply { addAll(item.categoryList,true) } }
        }
    } }

    override fun initData() {

        binding.rvCategoryMenu.adapter=menuAdapter
        binding.rvCommodityList.adapter=tableAdapter
    }

    override fun onSupportVisible() {
        loadPopup.show()
        viewModel.getCategoryData()
    }

    override fun initViewObservable() {
        viewModel.categoryNavVo.observe(this, Observer {
//            KLog.e(it.size)
            if (it.none {  it->it.isFold == 0 }) {
                it.first().isFold=0
                menuAdapter.oldPosition=0
            }
            menuAdapter.addAll(it,true)
//            binding.rvCategoryMenu.l
//            binding.rvCategoryMenu.get(0)?.performClick()
        })
        viewModel.stateLiveData.stateEnumMutableLiveData.observe(this, Observer {
            when(it){
                StateLiveData.StateEnum.Loading -> {
//                    loadingUtil?.showLoading("加载中..")
                    loadPopup.show()


                    KLog.e("请求数据中--显示loading")
                }
                StateLiveData.StateEnum.Success -> {
                    loadPopup.dismiss()
                    KLog.e("数据获取成功--关闭loading")
                }
                StateLiveData.StateEnum.Idle -> {
                    loadPopup.dismiss()


                    KLog.e("空闲状态--关闭loading")
//                    loadingUtil?.hideLoading()
                }
                StateLiveData.StateEnum.NoData -> {
                    loadPopup.dismiss()


                    KLog.e("空闲状态--关闭loading")
                }
                else -> {
                    loadPopup.dismiss()


                    KLog.e("其他状态--关闭loading")
//                    loadingUtil?.hideLoading()
                }
            }
        })
    }
}