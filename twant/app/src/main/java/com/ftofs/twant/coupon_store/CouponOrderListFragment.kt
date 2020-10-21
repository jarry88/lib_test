package com.ftofs.twant.coupon_store

import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.size
import com.bumptech.glide.Glide
import com.ftofs.lib_net.model.CouponItemVo
import com.ftofs.twant.BR
import com.ftofs.twant.R
import com.ftofs.twant.databinding.CouponOrderListFragmentBinding
import com.ftofs.twant.databinding.CouponStoreItemBinding
import com.ftofs.twant.kotlin.extension.dp2FloatPx
import com.ftofs.twant.kotlin.extension.dp2IntPx
import com.ftofs.twant.util.ToastUtil
import com.ftofs.twant.util.Util
import com.google.android.material.tabs.TabLayout
import com.gzp.lib_common.base.BaseTwantFragmentMVVM
import org.apache.http.cookie.SM


class CouponOrderListFragment: BaseTwantFragmentMVVM<CouponOrderListFragmentBinding, CouponStoreViewModel>(){
    override fun initContentView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): Int {
        return R.layout.coupon_order_list_fragment
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
            config<CouponItemVo, CouponStoreItemBinding>(R.layout.coupon_store_item, viewModel.couponStoreList){ b, d ->
                b.vo=d
                b.root.setOnClickListener { Util.startFragment(CouponStoreDetailFragment.newInstance(d.id)) }
            }
            setRefreshListener {
                viewModel.currPage=0
                viewModel.getActivityList(type) }
            setLoadMoreListener { viewModel.getActivityList(type) }
            mBinding.refreshLayout.autoRefresh()
        }
        binding.tabLayout.apply {
            setSelectedTabIndicatorColor(resources.getColor(R.color.tw_red))
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
//                customView = tagViewFactory("全部")
                text="全部"
            }
            )
            addTab(newTab().apply {
                text="待付款"

//                customView = tagViewFactory("待付款")
            })
            addTab(newTab().apply {
                text="可使用"
//
//                customView = tagViewFactory("可使用")

            })
            addTab(newTab().apply {
                text="退款/售後"
//                customView = tagViewFactory("退款/售後")

            })

            post {
                try {
                    //拿到tabLayout的mTabStrip属性
                    val mTabStripField = javaClass.getDeclaredField("mTabStrip")
                    mTabStripField.isAccessible=true
                    val mTabStrip = mTabStripField.get(this) as LinearLayout
                    for (i in 0 until mTabStrip.childCount) {
                        val tabView = mTabStrip.getChildAt(i)

                        //拿到tabView的mTextView属性
                        val mTextViewField = tabView.javaClass.getDeclaredField("mTextView")
                        mTextViewField.isAccessible = true
                        val mTextView = mTextViewField.get(tabView) as TextView
                        tabView.setPadding(0, 0, 0, 0)

                        //因为我想要的效果是   字多宽线就多宽，所以测量mTextView的宽度
                        var width = 0
                        width = mTextView.width
                        if (width == 0) {
                            mTextView.measure(0, 0)
                            width = mTextView.measuredWidth
                        }

                        //设置tab左右间距为10dp  注意这里不能使用Padding 因为源码中线的宽度是根据 tabView的宽度来设置的
                        val params = tabView.layoutParams as LinearLayout.LayoutParams
                        params.width = width
                        params.leftMargin = 10.dp2IntPx(_mActivity)
                        params.rightMargin = 10.dp2IntPx(_mActivity)
                        tabView.layoutParams = params
                        tabView.invalidate()
                    }
                } catch (e: NoSuchFieldException) {
                    e.printStackTrace()
                } catch (e: IllegalAccessException) {
                    e.printStackTrace()
                }
            }

        }
        binding.btnMoreCoupon.setOnClickListener { Util.startFragment(CouponActivityListFragment()) }
    }

    companion object{
        @JvmStatic
        fun newInstance()=CouponOrderListFragment().apply{
            arguments=Bundle().apply {
            }
        }
    }
    override fun initViewObservable() {
        viewModel.stateLiveData.stateEnumMutableLiveData.observe(this){
            binding
        }
    }
    private fun showDrawListView(v: View, selectedTabPosition: Int) {
        ToastUtil.success(context, "$selectedTabPosition")
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