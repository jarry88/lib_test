package com.ftofs.twant.coupon_store

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.ftofs.twant.BR
import com.ftofs.twant.R
import com.ftofs.twant.databinding.CouponStoreDetailFragmentBinding
import com.ftofs.twant.databinding.ImageSquareItemBinding
import com.ftofs.twant.dsl.customer.factoryAdapter
import com.ftofs.twant.dsl.imageUrl
import com.ftofs.twant.dsl.margin_end
import com.ftofs.twant.fragment.ViewPagerFragment
import com.ftofs.twant.util.ToastUtil
import com.ftofs.twant.util.User
import com.ftofs.twant.util.Util
import com.gzp.lib_common.base.BaseTwantFragmentMVVM
import com.gzp.lib_common.utils.SLog

private const val COUPON_ID ="couponId"
class CouponStoreDetailFragment():BaseTwantFragmentMVVM<CouponStoreDetailFragmentBinding,CouponStoreViewModel>() {
    override fun initContentView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): Int {
        return R.layout.coupon_store_detail_fragment
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    private val imageAdapter by lazy {  factoryAdapter<String,ImageSquareItemBinding>(R.layout.image_square_item){b,d->
        b.llContainer.apply {
            margin_end=8
        }
        b.imageItem.imageUrl=d
        b.imageItem.setOnClickListener{
            viewModel.currCouponDetail.value?.picList?.let {list ->
                Util.startFragment(ViewPagerFragment.newInstance(list,false).also { it.start=list.indexOf(d) })}
        }
    } }
    val id by lazy { arguments?.getInt(COUPON_ID) }
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
            viewModel.getCouponDetail(it)
        }?:viewModel.getCouponDetail(42)
        binding.rvImage.adapter=imageAdapter
        binding.btnBuy.setOnClickListener {
            if (User.getUserId() > 0) {
                Util.startFragment(CouponConfirmOrderFragment.newInstance(viewModel.currCouponDetail.value?.id))
            } else Util.showLoginFragment(requireContext())}
        binding.couponInformation.observable(viewModel.currCouponDetail)
    }

    companion object {
        @JvmStatic
        fun newInstance(couponId: Int?)=CouponStoreDetailFragment().apply {
            arguments = Bundle().apply {
                couponId?.let {
                    putInt(COUPON_ID,it).apply { SLog.info("couponId $it") }
                }
            }
        }
    }

    override fun initViewObservable() {
        viewModel.currCouponDetail.observe(this){
            binding.vo=it
            it.picList?.let {list ->
                imageAdapter.addAll(list,true)
            }
        }
    }
}

