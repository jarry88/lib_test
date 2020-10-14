package com.ftofs.twant.coupon_store

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.ftofs.twant.R
import com.ftofs.twant.BR
import com.ftofs.twant.databinding.CouponStoreDetailFragmentBinding
import com.gzp.lib_common.base.BaseTwantFragmentMVVM

private const val COUPON_ID ="couponId"
class ShopCouponStoreListFragment():BaseTwantFragmentMVVM<CouponStoreDetailFragmentBinding,CouponStoreViewModel>() {
    override fun initContentView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): Int {
        return R.layout.coupon_store_detail_fragment
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initData() {
        binding.title.apply {

        }
    }

    companion object {
        @JvmStatic
        fun newInstance(couponId: Int):ShopCouponStoreListFragment {
            val args = Bundle()
            args.putInt(COUPON_ID,couponId)
            val  fragment =ShopCouponStoreListFragment()
            fragment.arguments=args
            return fragment
        }
    }
}