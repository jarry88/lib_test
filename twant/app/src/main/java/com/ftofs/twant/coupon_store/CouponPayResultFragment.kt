package com.ftofs.twant.coupon_store

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.ftofs.twant.BR
import com.ftofs.twant.R
import com.ftofs.twant.databinding.CouponPayResultFragmentBinding
import com.ftofs.twant.dsl.*
import com.ftofs.twant.fragment.MainFragment
import com.ftofs.twant.kotlin.extension.p
import com.ftofs.twant.util.QRCode
import com.gzp.lib_common.base.BaseTwantFragmentMVVM
import com.gzp.lib_common.utils.SLog
import com.gzp.lib_common.utils.Util
import com.uuzuche.lib_zxing.activity.CodeUtils

private const val PAY_ID="pay_id"
private const val Success="success"

class CouponPayResultFragment:BaseTwantFragmentMVVM<CouponPayResultFragmentBinding, CouponStoreViewModel>() {
    override fun initContentView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): Int {
        return R.layout.coupon_pay_result_fragment
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }
    val payId by lazy { arguments?.getInt(PAY_ID) }
    val success by lazy { arguments?.getBoolean(Success)?:false }
    companion object {

        @JvmStatic
        fun newInstance(payId: Int?, success: Boolean = false)=CouponPayResultFragment().apply {
            arguments = Bundle().apply {
                payId?.let {
                    putInt(PAY_ID, it).apply { SLog.info("pay_id $it") }
                }
                putBoolean(Success, success)
            }
        }
    }

    override fun initData() {
        binding.success=success
        binding.title.apply {
            setLeftLayoutClickListener{onBackPressedSupport()}
        }
        binding.btnGotoHome.setOnClickListener {
            this.popTo(MainFragment::class.java, false)
        }
        binding.btnGotoOrder.setOnClickListener {
            com.ftofs.twant.util.Util.startFragment(CouponOrderDetailFragment.newInstance(id))
        }
        if (success) {
            viewModel.getCouponOrderDetail(id)
        }
    }

    override fun initViewObservable() {
        viewModel.currCouponOrder.observe(this){
            SLog.info("獲取訂單詳情")
            binding.listItem.mBinding.vo=it.itemList?.get(0)?.getCouponItemVo()
            binding.llCodeContainer.apply {
                it.itemList?.get(0)?.extractCode?.forEach {orderCodeVo ->
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
                        }}
                    //添加code码
                }
            }
        }
    }
}