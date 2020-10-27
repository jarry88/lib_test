package com.ftofs.twant.coupon_store

import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.ftofs.lib_net.model.CouponDetailVo
import com.ftofs.lib_net.model.CouponItemVo
import com.gzp.lib_common.base.BaseTwantFragmentMVVM
import com.ftofs.twant.R
import com.ftofs.twant.BR
import com.ftofs.twant.databinding.CouponActivityListFragmentBinding
import com.ftofs.twant.databinding.CouponStoreItemBinding
import com.ftofs.twant.util.ToastUtil
import com.ftofs.twant.util.Util
import com.google.android.material.tabs.TabLayout

class CouponActivityListFragment: BaseTwantFragmentMVVM<CouponActivityListFragmentBinding,CouponStoreViewModel>(){
    override fun initContentView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): Int {
        return R.layout.coupon_activity_list_fragment
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }
    var tabFold=true
    var firstTabSelected=true

    var type =""

    override fun initData() {
        binding.title.apply {
            setLeftImageResource(R.drawable.icon_back)
            setLeftLayoutClickListener{onBackPressedSupport()}
        }
        binding.smartList.apply {
            config<CouponDetailVo,CouponStoreItemBinding>(R.layout.coupon_store_item,viewModel.couponStoreList){b,d ->
                b.vo=d
                b.root.setOnClickListener { Util.startFragment(CouponStoreDetailFragment.newInstance(d.id)) }
            }
            setRefreshListener {
                viewModel.currPage=0
                viewModel.getActivityList(type=type) }
            setLoadMoreListener { viewModel.getActivityList(type=type) }
            mBinding.refreshLayout.autoRefresh()
        }
        binding.tabLayout.apply {
            setSelectedTabIndicatorColor(resources.getColor(R.color.tw_blue))
            addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    if (firstTabSelected) {
                        firstTabSelected = false
                        return
                    }
                    val iconView = tab?.customView?.findViewById<ImageView>(R.id.icon_exp)
                    val textView = tab?.customView?.findViewById<TextView>(R.id.tag_text)
                    textView?.setTextColor(resources.getColor(R.color.tw_blue))

                    iconView?.let {
                        Glide.with(context).load(R.drawable.up_arrow_blue).centerCrop().into(it)
                    }
                    tab?.customView?.let {
                        showDrawListView(it, selectedTabPosition)
                    }

                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                    val textView = tab?.customView?.findViewById<TextView>(R.id.tag_text)
                    textView?.setTextColor(resources.getColor(R.color.black))
                    val iconView = tab?.customView?.findViewById<ImageView>(R.id.icon_exp)
                    iconView?.let {
                        Glide.with(context).load(R.drawable.down_black_arrow).centerCrop().into(it)
                    }
                }

                override fun onTabReselected(tab: TabLayout.Tab?) {
                    val iconView = tab?.customView?.findViewById<ImageView>(R.id.icon_exp)

                    iconView?.let {
                        if (tabFold) {
                            Glide.with(context).load(R.drawable.up_arrow_blue).centerCrop().into(it)
                            tab.customView?.let { v ->
                                showDrawListView(v, selectedTabPosition)
                            }
                        } else {
                            Glide.with(context).load(R.drawable.down_arrow_blue).centerCrop().into(it)
                        }
                    }

                }

            })
            addTab(newTab().apply {
                customView = tagViewFactory("生活", true)
            }
            )
            addTab(newTab().apply {
                customView = tagViewFactory("價格")
            })
            addTab(newTab().apply {
                customView = tagViewFactory("更多")

            })

        }
    }

    override fun initViewObservable() {
        viewModel.stateLiveData.stateEnumMutableLiveData.observe(this){
            binding
        }
    }
    private fun showDrawListView(v: View, selectedTabPosition: Int) {
        ToastUtil.success(context,"$selectedTabPosition")
    }

    private fun tagViewFactory(tagText: String, visible: Boolean = true)=with(LayoutInflater.from(context).inflate(R.layout.tab_red_count_item, null, false)) {
        findViewById<TextView>(R.id.tag_text)?.apply {
            text=tagText
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 15.0f)
            setTextColor(resources.getColor(R.color.tw_black))
        }
        if(visible)findViewById<ImageView>(R.id.icon_exp)?.apply {
            visibility= View.VISIBLE
            Glide.with(context).load(R.drawable.down_black_arrow).centerCrop().into(this)
        }
        this
    }
}