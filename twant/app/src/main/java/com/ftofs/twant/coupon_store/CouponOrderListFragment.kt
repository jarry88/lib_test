package com.ftofs.twant.coupon_store

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.map
import com.bumptech.glide.Glide
import com.ftofs.lib_net.model.CouponOrderBase
import com.ftofs.twant.BR
import com.ftofs.twant.R
import com.ftofs.twant.activity.MainActivity
import com.ftofs.twant.appserver.AppServiceImpl.Companion.getCaptureIntent
import com.ftofs.twant.constant.EBMessageType
import com.ftofs.twant.databinding.CouponOrderListFragmentBinding
import com.ftofs.twant.databinding.CouponOrderListItemBinding
import com.ftofs.twant.dsl.*
import com.ftofs.twant.dsl.customer.toMopString
import com.ftofs.twant.entity.EBMessage
import com.ftofs.twant.interfaces.OnConfirmCallback
import com.ftofs.twant.kotlin.extension.dp2IntPx
import com.ftofs.twant.kotlin.setVisibleOrGone
import com.ftofs.twant.util.ToastUtil
import com.ftofs.twant.util.Util
import com.ftofs.twant.widget.TwConfirmPopup
import com.google.android.material.tabs.TabLayout
import com.gzp.lib_common.base.BaseTwantFragmentMVVM
import com.gzp.lib_common.utils.SLog
import com.lxj.xpopup.XPopup
import com.macau.pay.sdk.MPaySdk
import com.wzq.mvvmsmart.event.StateLiveData
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


class CouponOrderListFragment: BaseTwantFragmentMVVM<CouponOrderListFragmentBinding, CouponStoreViewModel>(){
    override fun initContentView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): Int {
        return R.layout.coupon_order_list_fragment
    }
    override fun initVariableId(): Int {
        return BR.viewModel
    }

    private var showResult: Boolean=false
    var tabFold=true
    var firstTabSelected=true

    var type =""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EventBus.getDefault().register(this)
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEBMessage(message: EBMessage) {
        when (message.messageType) {
            EBMessageType.MESSAGE_TYPE_COUPON_MPAY_SUCCESS -> {

//                viewModel.postMpayNotify()
                if (isSupportVisible) {
                    showResultFragment()

                } else {
                    showResult = true
                }
            }
            EBMessageType.COUPON_CANCEL_SHOW_RESULT_SUCCESS -> showResult = false
            EBMessageType.MESSAGE_TYPE_COUPON_MPAY_OTHER -> {
                if (isSupportVisible) {
                    SLog.info("支付失败")
//                    Util.startFragment(CouponPayResultFragment.newInstance(Hawk.get(SPField.FIELD_MPAY_PAY_ID)))
                }
            }
            else ->{}//SLog.info(this::class.java.name)
        }
    }

    private fun showResultFragment() {
        EBMessage.postMessage(EBMessageType.COUPON_CANCEL_SHOW_RESULT_SUCCESS, null)
        Util.startFragment(CouponPayResultFragment.newInstance(
                viewModel.currOrderId //Hawk.get(SPField.FIELD_MPAY_PAY_ID)
                , true)).apply { SLog.info("由 ${this.javaClass.name}拉起") }
    }

    override fun onSupportVisible() {
        super.onSupportVisible()
        if (showResult) {
           showResultFragment()
        }
    }

    override fun onSupportInvisible() {
        super.onSupportInvisible()
        showResult =false
    }
    override fun initData() {
        binding.title.apply {
            setLeftImageResource(R.drawable.icon_back)
            setLeftLayoutClickListener{onBackPressedSupport()}
        }
        binding.smartList.apply {
            config<CouponOrderBase, CouponOrderListItemBinding>(R.layout.coupon_order_list_item, viewModel.couponOrdersListInfo.map { it.list }){ b, d ->
                b.vo
                b.tvStatus.text=d.getOrderStatusString()
                b.tvStatus.colorId=if(d.getOrderRed()) R.color.tw_red else R.color.tw_black
                b.llContainer.apply {
                    removeAllViews()
                    d.itemList?.forEach { vo ->
                        addView(LinearLayout {//第一行
                            layout_width = match_parent
                            layout_height = wrap_content
                            margin_end = 16
                            margin_bottom=16
                            orientation = horizontal

                            TextView {
                                layout_width = wrap_content
                                layout_height = wrap_content
                                setLines(1)
                                ellipsize = TextUtils.TruncateAt.END
                                textStyle = bold
                                textSize = 14f
                                colorId = R.color.black
                                text = vo.title
                            }
                            View {
                                layout_width = 0
                                layout_height = 0
                                margin_end = 72
                                weight = 1f
                            }

                        }.let {
                            (it.parent as ViewGroup).removeView(it)
                            it
                        }
                        )
                        addView(LinearLayout {//第二行
                            layout_width = match_parent
                            layout_height = wrap_content
                            margin_end = 16
                            orientation = horizontal

                            ImageView {
                                layout_width = 64
                                layout_height = 64
                                imageUrl = vo.cover
                                circle_radius = 2
                                margin_end = 10

                            }
                            LinearLayout {
                                layout_width = match_parent
                                layout_height = wrap_content
                                orientation = vertical
                                TextView {
                                    layout_width = wrap_content
                                    layout_height = wrap_content
                                    textSize = 14f
                                    textStyle = bold
                                    colorId = R.color.black
                                    margin_bottom = 8
                                    text = vo.price.toMopString
                                }
                                TextView {
                                    layout_width = wrap_content
                                    layout_height = wrap_content
                                    textSize = 12f
                                    colorId = R.color.tw_grey_666
                                    margin_bottom = 8
                                    text = "數量：${vo.num}"
                                }
                                TextView {
                                    layout_width = wrap_content
                                    layout_height = wrap_content
                                    textSize = 12f
                                    colorId = R.color.tw_grey_666
                                    text = "下單時間：${d.createTime}"
                                }
                            }

                        }.let {
                            (it.parent as ViewGroup).removeView(it)
                            it
                        }
                        )
                    }
                }
                b.btnCancel.setVisibleOrGone(d.orderStatus?.let { it == 10 } ?: false)
                b.btnCancel.setOnClickListener {
//                    viewModel.deleteCouponOrderDetail(it.id)
                    cancelOrder(d.id)

                }
                b.btnGotoPay.setOnClickListener {
                    viewModel.currOrderId=d.id
                    viewModel.loadMpay(mapOf("clientType" to "android", "orderId" to d.id))
                }
                b.btnGotoPay.setVisibleOrGone(d.orderStatus?.let { it == 10 } ?: false)
                b.root.setOnClickListener {
                    when (it.id) {
                        R.id.btn_goto_pay -> Util.startFragment(CouponConfirmOrderFragment.newInstance(d.id))
                        R.id.btnCancel -> viewModel.putCouponOrderDetail(it.id)
                        else ->Util.startFragment(CouponOrderDetailFragment.newInstance(d.id))
                    }
                }
                b.root.setOnLongClickListener { cancelOrder(d.id, true)
                true}
//                b.btnGotoRefund.setVisibleOrGone(false) //現階段退款隱藏
//                b.btnGotoRefund.setVisibleOrGone(d.orderStatus?.let { it==20 }?:false)
            }
            setRefreshListener {
                viewModel.currPage=0
                viewModel.getOrdersList() }
            setLoadMoreListener { viewModel.getOrdersList() }
            mBinding.refreshLayout.autoRefresh()
        }
        binding.tabLayout.apply {
            setSelectedTabIndicatorColor(resources.getColor(R.color.tw_red, null))
            addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    if (firstTabSelected) {
                        firstTabSelected = false
                        return
                    }
                    viewModel.currOrderStatus.postValue(when (selectedTabPosition) {
                        0 -> null //全部
                        1 -> 10 //待付款
                        2 -> 20  // 可使用
                        3 -> 40 // 退款中
                        else -> null
                    })
                    val iconView = tab?.customView?.findViewById<ImageView>(R.id.icon_exp)
                    val textView = tab?.customView?.findViewById<TextView>(R.id.tag_text)
                    textView?.setTextColor(resources.getColor(R.color.tw_blue))

                    iconView?.let {
                        Glide.with(context).load(R.drawable.up_arrow_blue).centerCrop().into(it)
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
                text = "全部"
            }
            )
            addTab(newTab().apply {
                text = "待付款"

//                customView = tagViewFactory("待付款")
            })
            addTab(newTab().apply {
                text = "可使用"
//
//                customView = tagViewFactory("可使用")

            })
            addTab(newTab().apply {
                text = "退款/售後"
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

    private fun cancelOrder(id: Int?, deleteOrCancel: Boolean = false) {
        var confirmText = "確定要取消訂單嗎?"
                   if (deleteOrCancel ) {
                        confirmText = "確定要刪除訂單嗎?"
                    }
        XPopup.Builder(_mActivity) //                         .dismissOnTouchOutside(false)
                // 设置弹窗显示和隐藏的回调监听
                //                         .autoDismiss(false)
                .asCustom(TwConfirmPopup(_mActivity, confirmText, null, object : OnConfirmCallback {
                    override fun onYes() {
                        SLog.info("onYes")
                        id?.let {
                            if (deleteOrCancel)
                                viewModel.deleteCouponOrderDetail(it)
                            else
                                viewModel.putCouponOrderDetail(it)
                        }
                    }

                    override fun onNo() {
                        SLog.info("onNo")
                    }
                }))
                .show()
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
            binding.smartList.endLoadingUi()
            if (it == StateLiveData.StateEnum.Idle) {
                binding.smartList.autoRefresh()
            }
        }
        viewModel.mPayVo.observe(this){
            if (it.isPay) {
                ToastUtil.success(context, "已經支付過了")
            } else {
                MPaySdk.mPayNew(_mActivity, it.payData.toString().apply { SLog.info(this) }, _mActivity as MainActivity)
            }
        }
        viewModel.currOrderStatus.observe(this){binding.smartList.autoRefresh().apply { SLog.info("刷新") }}
        viewModel.error.observe(this){
            if (!it.isNullOrEmpty()) {
                ToastUtil.error(context, it)
                binding.smartList.autoRefresh()
            }
        }
    }
    private fun showDrawListView(v: View, selectedTabPosition: Int) {
        ToastUtil.success(context, "$selectedTabPosition")
    }
}