package com.ftofs.twant.coupon_store

import android.graphics.Color
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.map
import com.ftofs.twant.BR
import com.ftofs.twant.R
import com.ftofs.twant.databinding.CouponStoreDetailFragmentBinding
import com.ftofs.twant.databinding.ImageSquareItemBinding
import com.ftofs.twant.dsl.*
import com.ftofs.twant.kotlin.extension.removeParent
import com.ftofs.twant.util.ToastUtil
import com.ftofs.twant.view.SmartListView
import com.gzp.lib_common.base.BaseTwantFragmentMVVM

private const val COUPON_ID ="couponId"
class CouponStoreDetailFragment():BaseTwantFragmentMVVM<CouponStoreDetailFragmentBinding,CouponStoreViewModel>() {
    override fun initContentView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): Int {
        return R.layout.coupon_store_detail_fragment
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }
    val id=arguments?.getInt(COUPON_ID)

    override fun initData() {
        binding.title.apply {
            setLeftImageResource(R.drawable.icon_back)
//            setRightImageResource(R.drawable.icon_coupon_share)
            setRightLayoutClickListener{ToastUtil.success(context,"分享")}
            setLeftLayoutClickListener{onBackPressedSupport()}
        }
        id?.let {
            viewModel.getCouponDetail(it)
        }?:viewModel.getCouponDetail(42)
        binding.imageList.apply {
            setOrientation()
            config<String,ImageSquareItemBinding>(R.layout.image_square_item,viewModel.currCouponDetail.map { it.picList }){b,d->
                b.imageItem.imageUrl=d
            }
        }
        binding.couponInformation.observable(viewModel.currCouponDetail)
    }

    companion object {
        @JvmStatic
        fun newInstance(couponId: Int?):CouponStoreDetailFragment {
            val args = Bundle()
            couponId?.let {
                args.putInt(COUPON_ID,it)
            }
            val  fragment =CouponStoreDetailFragment()
            fragment.arguments=args
            return fragment
        }
    }

    override fun initViewObservable() {
        viewModel.currCouponDetail.observe(this){
            binding.vo=it
        }
    }
}

