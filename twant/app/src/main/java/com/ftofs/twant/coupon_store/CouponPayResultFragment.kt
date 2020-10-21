package com.ftofs.twant.coupon_store

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.ftofs.twant.R
import com.ftofs.twant.BR
import com.ftofs.twant.databinding.CouponPayResultFragmentBinding
import com.gzp.lib_common.base.BaseTwantFragmentMVVM
import com.gzp.lib_common.utils.SLog

private const val PAY_ID="pay_id"
private const val Success="success"

class CouponPayResultFragment:BaseTwantFragmentMVVM<CouponPayResultFragmentBinding,CouponStoreViewModel>() {
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
        fun newInstance(payId: Int?, success:Boolean=false)=CouponPayResultFragment().apply {
            arguments = Bundle().apply {
                payId?.let {
                    putInt(PAY_ID,it).apply { SLog.info("pay_id $it") }
                }
                putBoolean(Success,success)
            }
        }
    }

    override fun initData() {
        binding.success=success
    }
}