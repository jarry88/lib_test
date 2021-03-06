package com.ftofs.twant.go853

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.ftofs.lib_net.model.PropertyVo
import com.ftofs.twant.R
import com.ftofs.twant.BR
import com.ftofs.twant.databinding.GoIntermediaryListFragmentBinding
import com.ftofs.twant.databinding.ItemHouseVoBinding
import com.ftofs.twant.kotlin.adapter.DataBoundAdapter
import com.ftofs.twant.util.StringUtil
import com.ftofs.twant.util.Util
import com.gzp.lib_common.base.BaseTwantFragmentMVVM
import com.gzp.lib_common.utils.SLog
import com.wzq.mvvmsmart.utils.KLog

class GoIntermediaryListFragment @JvmOverloads constructor(private val uid: Int?=null) : BaseTwantFragmentMVVM<GoIntermediaryListFragmentBinding,GoHouseViewModel>() {
    override fun initContentView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): Int {
        return R.layout.go_intermediary_list_fragment
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }
    private val mAdapter by lazy {
        object : DataBoundAdapter<PropertyVo, ItemHouseVoBinding>(emptyId = R.layout.go_placeholder_no_data){
            override val layoutId: Int
                get() = R.layout.item_house_vo

            override fun initView(binding: ItemHouseVoBinding, item: PropertyVo) {
                binding.vo=item
                binding.root.setOnClickListener {
                    Util.startFragment(GoPropertyDetailFragment(item.pid, item))
                }
            }

        }.apply { showEmptyView(true) }
    }
    override fun initData() {
        KLog.init(true)
        binding.title.apply {
            setLeftLayoutClickListener() { onBackPressedSupport() }
            setLeftImageResource(R.drawable.icon_back)
        }
        viewModel.currUid=uid
        binding.refreshLayout.setOnRefreshListener {
            viewModel.currPage=0
            viewModel.getUserPropertyList()
        }
        binding.refreshLayout.setNoMoreData(true)
        binding.rvList.adapter=mAdapter
        binding.refreshLayout.setOnLoadMoreListener { viewModel.getUserPropertyList() }
        uid?.let { binding.refreshLayout.autoRefresh() }?:hideSoftInputPop()

    }

    override fun initViewObservable() {
        viewModel.userPropertyList.observe(this){
            binding.vo=it.user
            binding.title.text=it.user?.propertyCorp
            val url =it.user?.userPhoto
            SLog.info(url)
            url?.let {
                if (url.isNotEmpty()) {
                    if (!url.contains("none.gif")) {//go853房产占位图
                        SLog.info("here $url|")
                        Glide.with(_mContext).load(StringUtil.normalizeImageUrl(url)).centerCrop().into(binding.imgAvatar)
                }
            }

            }



            mAdapter.addAll(it.propertyList?: listOf(),viewModel.currPage<=1)
        }
        viewModel.stateLiveData.stateEnumMutableLiveData.observe(this){
            binding.refreshLayout.finishRefresh()
            binding.refreshLayout.finishLoadMore()
            if(!viewModel.hasMore)binding.refreshLayout.setNoMoreData(true)
        }
    }

}
