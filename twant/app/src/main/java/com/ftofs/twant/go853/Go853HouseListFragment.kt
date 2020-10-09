package com.ftofs.twant.go853

import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.ftofs.lib_net.model.PropertyVo
import com.ftofs.twant.BR
import com.ftofs.twant.R
import com.ftofs.twant.databinding.GoHouseListFragmentBinding
import com.ftofs.twant.databinding.ItemHouseVoBinding
import com.ftofs.twant.dsl.*
import com.ftofs.twant.kotlin.adapter.DataBoundAdapter
import com.gzp.lib_common.base.BaseTwantFragmentMVVM
import com.gzp.lib_common.utils.SLog
import com.lyrebirdstudio.croppylib.util.extensions.visible

class Go853HouseListFragment :BaseTwantFragmentMVVM<GoHouseListFragmentBinding, GoHouseViewModel>(){
    override fun initContentView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): Int {
        return R.layout.go_house_list_fragment
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }
    private val mAdapter by lazy {
        object :DataBoundAdapter<PropertyVo, ItemHouseVoBinding>(){
            override val layoutId: Int
                get() = R.layout.item_house_vo

            override fun initView(binding: ItemHouseVoBinding, item: PropertyVo) {
                binding.vo=item
                binding.root.setOnClickListener {
                    start(GoPropertyDetailFragment(item.pid,item))
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
                customView =tagViewFactory("房產",true)
//                customView=android.widget.LinearLayout(context)
            })
            addTab(newTab().apply {
                customView = tagViewFactory("租售")
            })
            addTab(newTab().apply {
                customView = tagViewFactory("區域")

            })
            addTab(newTab().apply {
                customView = tagViewFactory("價格")

            })
        }
        binding.rvList.adapter=mAdapter
        viewModel.getPropertyList()
    }

    override fun initViewObservable() {
        viewModel.propertyList.observe(this){
            SLog.info("觀測到數據變化")
            it.propertyList?.apply {
                SLog.info("觀測到數據變化${it.propertyList?.size}")

                mAdapter.addAll(this,it.pageEntity.curPage==1)
            }
        }
    }
    private fun tagViewFactory(tagText:String,visible:Boolean=true)=with(LayoutInflater.from(context).inflate(R.layout.tab_red_count_item, null, false)) {
        findViewById<TextView>(R.id.tag_text)?.apply {
            text=tagText
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 15.0f)
            setTextColor(resources.getColor(R.color.tw_black))
        }
        if(visible)findViewById<ImageView>(R.id.icon_exp)?.visibility=View.VISIBLE
        this
    }
}