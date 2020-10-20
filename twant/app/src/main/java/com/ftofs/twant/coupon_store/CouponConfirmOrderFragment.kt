package com.ftofs.twant.coupon_store

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.ftofs.lib_net.model.BuyGoodsDTO
import com.ftofs.lib_net.model.BuyStep1Vo
import com.ftofs.lib_net.model.CouponItemVo
import com.ftofs.lib_net.smart.net_utils.GsonUtil
import com.ftofs.twant.BR
import com.ftofs.twant.R
import com.ftofs.twant.config.Config
import com.ftofs.twant.databinding.CouponOrderConfirmFragmentBinding
import com.ftofs.twant.databinding.CouponOrderConfirmItemBinding
import com.ftofs.twant.dsl.customer.factoryAdapter
import com.ftofs.twant.util.AssetsUtil
import com.google.gson.Gson
import com.gzp.lib_common.base.BaseTwantFragmentMVVM
import com.gzp.lib_common.utils.BaseContext
import com.gzp.lib_common.utils.SLog
import com.gzp.lib_common.utils.Util
import com.wzq.mvvmsmart.event.StateLiveData

private const val COUPON_ID ="orderId"

/**
 * 購買第一步：顯示票券信息
 * 券倉確認訂單頁
 */
class CouponConfirmOrderFragment:BaseTwantFragmentMVVM<CouponOrderConfirmFragmentBinding,CouponStoreViewModel>() {
    override fun initContentView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): Int {
        return R.layout.coupon_order_confirm_fragment
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    private val mAdapter by lazy {
        factoryAdapter<CouponItemVo,CouponOrderConfirmItemBinding>(R.layout.coupon_order_confirm_item){b,d ->
            b.vo=d
        }
    }
    val id by lazy { arguments?.getInt(COUPON_ID).apply { SLog.info(this.toString()) } }


    companion object {
        @JvmStatic
        fun newInstance(orderid: Int?):CouponConfirmOrderFragment {
            val args = Bundle()
            orderid?.let {
                args.putInt(COUPON_ID,it)
            }
            val  fragment =CouponConfirmOrderFragment()
            fragment.arguments=args
            return fragment
        }
    }

    override fun initData() {
        binding.title.apply {
//            setRightLayoutClickListener{ToastUtil.success(context,"分享")}
            setLeftLayoutClickListener{onBackPressedSupport()}
        }
        binding.rvList.adapter=mAdapter
        id?.let {
            viewModel.getTcBuyStep1(listOf(BuyGoodsDTO(it,1)))
        }?:hideSoftInputPop()
    }
    override fun initViewObservable() {
        viewModel.buyStep1Vo.observe(this){
            binding.vo=it
            it.couponBaseList?.let { list ->
                mAdapter.addAll(list,true)
            }
        }
        viewModel.stateLiveData.stateEnumMutableLiveData.observe(this){
            if (it == StateLiveData.StateEnum.Error) {
                if (Config.USE_DEVELOPER_TEST_DATA) {
                    viewModel.buyStep1Vo.postValue(GsonUtil.gson2Bean(AssetsUtil.loadText(BaseContext.instance.getContext(),"tangram/confirm_order.json"),BuyStep1Vo::class.java))
                }
            }
        }
    }
}

