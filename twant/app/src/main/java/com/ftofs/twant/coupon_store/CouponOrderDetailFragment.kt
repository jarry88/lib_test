package com.ftofs.twant.coupon_store

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.map
import com.ftofs.lib_net.model.OrderCodeVo
import com.ftofs.twant.BR
import com.ftofs.twant.R
import com.ftofs.twant.databinding.CouponOrderDetailFragmentBinding
import com.ftofs.twant.dsl.*
import com.ftofs.twant.kotlin.extension.p
import com.ftofs.twant.kotlin.setVisibleOrGone
import com.ftofs.twant.util.QRCode
import com.ftofs.twant.util.ToastUtil
import com.ftofs.twant.util.User
import com.ftofs.twant.util.Util
import com.google.zxing.BarcodeFormat
import com.gzp.lib_common.base.BaseTwantFragmentMVVM
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
        id?.apply { SLog.info("receive couponId $this") }
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
        fun newInstance(couponId: Int?)=CouponOrderDetailFragment().apply {
            arguments = Bundle().apply {
                couponId?.let {
                    putInt(ORDER_ID, it).apply { SLog.info("order_Id $it") }
                }
            }
        }
    }

    override fun initViewObservable() {
        viewModel.currCouponOrder.observe(this){
            binding.vo=it
            binding.llCodeContainer.apply { 
                removeAllViews()
                it.itemList?.forEach{ orderItem ->  
                    orderItem.extractCode?.forEach{ orderCodeVo ->
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
                                    TextView {
                                        layout_height = wrap_content
                                        layout_width = wrap_content
                                        textSize =18f
                                        text ="已過期"
                                        margin_start =8
                                        colorId =R.color.tw_black
                                        margin_end =4
                                    }
                                }
                            }

                        }.let { v -> (v.parent as ViewGroup).removeView(v)
                            addView(v).apply { SLog.info("添加二維碼${orderCodeVo.code}") }
                        }
                        orderCodeVo.used?.let {
                            if (!it) {
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
                                        margin_start=45
                                    }
                                    ImageView {
                                        layout_height = 160
                                        layout_width = 80
                                        margin_end =16
                                        setImageBitmap( QRCode.encode(orderCodeVo.code,80,160))
//                                        setImageBitmap(CodeUtils.createImage(orderCodeVo.code, 160, 160, null))

                                    }

                                }.let { v -> (v.parent as ViewGroup).removeView(v)
                                    addView(v)
                                }.p("aa")
                            }
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

