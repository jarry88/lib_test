package com.ftofs.twant.coupon_store

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.map
import com.ftofs.twant.BR
import com.ftofs.twant.R
import com.ftofs.twant.databinding.CouponOrderDetailFragmentBinding
import com.ftofs.twant.dsl.*
import com.ftofs.twant.kotlin.extension.p
import com.ftofs.twant.kotlin.setVisibleOrGone
import com.ftofs.twant.util.QRCodeUtil
import com.ftofs.twant.util.ToastUtil
import com.ftofs.twant.util.User
import com.ftofs.twant.util.Util
import com.gzp.lib_common.base.BaseTwantFragmentMVVM
import com.gzp.lib_common.base.Jarbon
import com.gzp.lib_common.utils.SLog
import com.uuzuche.lib_zxing.activity.CodeUtils

private const val ORDER_ID ="couponId"
class CouponOrderDetailFragment():BaseTwantFragmentMVVM<CouponOrderDetailFragmentBinding, CouponStoreViewModel>() {
    override fun initContentView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): Int {
        return R.layout.coupon_order_detail_fragment
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }


    val id by lazy { arguments?.getInt(ORDER_ID) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        id?.apply { SLog.info("receive orderid $this") }
    }
    override fun initData() {
        binding.title.apply {
            setLeftImageResource(R.drawable.icon_back)
//            setRightImageResource(R.drawable.icon_coupon_share)
            setRightLayoutClickListener{ToastUtil.success(context, "分享")}
            setLeftLayoutClickListener{onBackPressedSupport()}
        }
        binding.couponListItem.mBinding.apply {
            tvBottomPrice.setVisibleOrGone(true)
            tvValidity.setVisibleOrGone(true)
        }
        id?.let {
            viewModel.getCouponOrderDetail(it)
        }?:hideSoftInputPop()
        binding.btnBuyAgain.setOnClickListener {
            if (User.getUserId() > 0) {
                viewModel.currCouponOrder.value?.itemList?.get(0)?.coupon?.let {coupon ->
                    Util.startFragment(CouponStoreDetailFragment.newInstance(coupon.id))
                }
            } else Util.showLoginFragment(requireContext())}
        binding.couponInformation.observable(viewModel.currCouponOrder.map { it.itemList?.get(0)?.coupon })
    }

    companion object {
        @JvmStatic
        fun newInstance(orderId: Int?)=CouponOrderDetailFragment().apply {
            arguments = Bundle().apply {
                orderId?.let {
                    putInt(ORDER_ID, it).apply { SLog.info("order_Id $it") }
                }
            }
        }
    }

    override fun initViewObservable() {
        viewModel.currCouponOrder.observe(this){
            binding.vo=it
            binding.couponInformation.apply {
                (it.itemList?.get(0)?.consumptionType==1).let {b ->
                    mBinding.llMixContainer.setVisibleOrGone(b)
                }
            }
            binding.llCodeContainer.apply { 
                removeAllViews()
                it.itemList?.forEach{ orderItem ->  
                    orderItem.extractCode?.forEach{ orderCodeVo ->
                        var showCode= true
                        LinearLayout {
                            layout_height = wrap_content
                            layout_width = wrap_content
                            orientation = horizontal
                            center_vertical =true
                            margin_top = 17
                            margin_bottom=18
                            TextView {
                                layout_height = wrap_content
                                layout_width = wrap_content
                                textSize =14f
                                text ="取貨碼："
                                colorId =R.color.tw_black
                                margin_end =4
                            }
                            TextView {
                                layout_height = wrap_content
                                layout_width = wrap_content
                                textSize =18f
                                text =orderCodeVo.code
                                textStyle = bold
                                colorId =R.color.tw_black
                                margin_end =4
                            }
                            orderCodeVo.used?.let {
                                if (it) {//已經用過了
                                    showCode =false
                                    TextView {
                                        layout_height = wrap_content
                                        layout_width = wrap_content
                                        textSize =18f
                                        text = "已使用"
                                        margin_start =8
                                        colorId =R.color.tw_black
                                        margin_end =4
                                    }
                                }
                            }
                            if (showCode) {
                                //isValidity
                                orderItem.validityEndDate?.let {
                                    if(Jarbon.parse(it).timestamp<Jarbon().timestamp){
                                        showCode =false
                                        TextView {
                                            layout_height = wrap_content
                                            layout_width = wrap_content
                                            textSize =18f
                                            text = "已过期"
                                            margin_start =8
                                            colorId =R.color.tw_black
                                            margin_end =4
                                        }

                                    }
                                }
                            }

                        }.let { v -> (v.parent as ViewGroup).removeView(v)
                            addView(v).apply { SLog.info("添加二維碼${orderCodeVo.code}") }
                        }
                        if (showCode) {
                                LinearLayout {
                                    layout_height = wrap_content
                                    layout_width = wrap_content
                                    orientation = horizontal
                                    center_vertical =true
                                    margin_top = 17
                                    margin_bottom=18
                                    ImageView {
                                        layout_height = 160
                                        layout_width = 160
                                        margin_end =16
                                        setImageBitmap(CodeUtils.createImage(orderCodeVo.code, 160, 160, null))
                                        margin_start=30
                                    }
                                    ImageView {
                                        layout_height = 160
                                        layout_width = 80
                                        margin_end =16
                                        setImageBitmap( QRCodeUtil.createBarCode(orderCodeVo.code,220.dp,86.dp,null,Color.BLACK))
//                                        setImageBitmap(CodeUtils.createImage(orderCodeVo.code, 160, 160, null))

                                    }

                                }.let { v -> (v.parent as ViewGroup).removeView(v)
                                    addView(v)
                                }.p("aa")
                            }

                    }
                }
            }
            binding.btnLink.setOnClickListener { _ ->
                it.itemList?.get(0)?.coupon?.store?.apply {
                    Util.dialPhone(activity, contact)
                }
                }
        }
    }
}

