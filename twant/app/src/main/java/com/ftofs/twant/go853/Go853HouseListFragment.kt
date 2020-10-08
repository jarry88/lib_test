package com.ftofs.twant.go853

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.ftofs.lib_net.model.PropertyVo
import com.ftofs.twant.R
import com.ftofs.twant.BR
import com.ftofs.twant.databinding.GoHouseListFragmentBinding
import com.ftofs.twant.databinding.ItemHouseVoBinding
import com.ftofs.twant.dsl.*
import com.ftofs.twant.kotlin.adapter.DataBoundAdapter
import com.ftofs.twant.util.Util
import com.google.android.material.tabs.TabLayout
import com.gzp.lib_common.base.BaseTwantFragmentMVVM

class Go853HouseListFragment :BaseTwantFragmentMVVM<GoHouseListFragmentBinding,GoHouseViewModel>(){
    override fun initContentView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): Int {
        return R.layout.go_house_list_fragment
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }
    private val mAdapter by lazy {
        object :DataBoundAdapter<PropertyVo,ItemHouseVoBinding>(){
            override val layoutId: Int
                get() = R.layout.item_house_vo

            override fun initView(binding: ItemHouseVoBinding, item: PropertyVo) {
                binding.vo=item
                binding.root.setOnClickListener {
                    start(GoPropertyDetailFragment(item.pid))
                }
            }

        }
    }

    override fun initData() {
        binding.title.apply {
            text ="房產"
            setLeftImageResource(R.drawable.icon_back)
            setLeftLayoutClickListener{onBackPressedSupport()}
        }
        binding.banner.apply {
            setBackgroundResource(R.drawable.go_banner)
        }
        binding.tabLayout.apply {
            addTab(newTab().apply {
                customView=LinearLayout {
                    layout_width= match_parent
                    layout_height= match_parent
                    orientation= horizontal
                    center_vertical=true
                    TextView {  }
                    ImageView { src=R.drawable.ic_arrow_drop_down_black_24dp }
                }
            })
            addTab(newTab().apply {
                text="租售"
            })
            addTab(TabLayout.Tab().apply {
                text="區域"
            })
            addTab(TabLayout.Tab().apply {
                text="價格"
            })
        }
        binding.rvList.adapter=mAdapter
    }

}