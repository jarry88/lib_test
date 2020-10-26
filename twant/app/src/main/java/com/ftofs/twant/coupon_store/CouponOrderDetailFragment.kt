package com.ftofs.twant.coupon_store

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.ftofs.twant.BR
import com.ftofs.twant.R
import com.ftofs.twant.databinding.CouponOrderDetailFragmentBinding
import com.ftofs.twant.util.ToastUtil
import com.ftofs.twant.util.User
import com.ftofs.twant.util.Util
import com.gzp.lib_common.base.BaseTwantFragmentMVVM
import com.gzp.lib_common.utils.SLog

private const val ORDER_ID ="couponId"
class CouponOrderDetailFragment():BaseTwantFragmentMVVM<CouponOrderDetailFragmentBinding,CouponStoreViewModel>() {
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
            setRightLayoutClickListener{ToastUtil.success(context,"分享")}
            setLeftLayoutClickListener{onBackPressedSupport()}
        }
        id?.let {
            viewModel.getCouponOrderDetail(it)
        }?:hideSoftInputPop()
        binding.btnBuy.setOnClickListener {
            if (User.getUserId() > 0) {
                Util.startFragment(CouponConfirmOrderFragment.newInstance(viewModel.currCouponDetail.value?.id))
            } else Util.showLoginFragment(requireContext())}
        binding.couponInformation.observable(viewModel.currCouponDetail)
    }

    companion object {
        @JvmStatic
        fun newInstance(couponId: Int?)=CouponOrderDetailFragment().apply {
            arguments = Bundle().apply {
                couponId?.let {
                    putInt(ORDER_ID,it).apply { SLog.info("couponId $it") }
                }
            }
        }
    }

    override fun initViewObservable() {
        viewModel.currCouponOrder.observe(this){
            binding.vo=it
        }
    }
}

