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
import com.ftofs.twant.activity.MainActivity
import com.ftofs.twant.config.Config
import com.ftofs.twant.constant.EBMessageType
import com.ftofs.twant.constant.SPField
import com.ftofs.twant.databinding.CouponOrderConfirmFragmentBinding
import com.ftofs.twant.databinding.CouponOrderConfirmItemBinding
import com.ftofs.twant.dsl.customer.factoryAdapter
import com.ftofs.twant.entity.EBMessage
import com.ftofs.twant.fragment.PaySuccessFragment
import com.ftofs.twant.kotlin.adapter.DataBoundAdapter
import com.ftofs.twant.util.AssetsUtil
import com.ftofs.twant.util.ToastUtil
import com.ftofs.twant.util.Util
import com.gzp.lib_common.base.BaseTwantFragmentMVVM
import com.gzp.lib_common.utils.BaseContext
import com.gzp.lib_common.utils.SLog
import com.macau.pay.sdk.MPaySdk
import com.orhanobut.hawk.Hawk
import com.wzq.mvvmsmart.event.StateLiveData
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

private const val ORDER_ID ="orderId"

/**
 * 購買第一步：顯示票券信息
 * 券倉確認訂單頁
 */
class CouponConfirmOrderFragment:BaseTwantFragmentMVVM<CouponOrderConfirmFragmentBinding, CouponStoreViewModel>() {
    override fun initContentView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): Int {
        return R.layout.coupon_order_confirm_fragment
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    private val mAdapter by lazy {
//        factoryAdapter<CouponItemVo, CouponOrderConfirmItemBinding>(R.layout.coupon_order_confirm_item){ b, d ->
//            b.vo=d
//        }
        object :DataBoundAdapter<CouponItemVo,CouponOrderConfirmItemBinding>(){
            override val layoutId: Int
                get() = R.layout.coupon_order_confirm_item

            override fun initView(binding: CouponOrderConfirmItemBinding, item: CouponItemVo) {
                binding.vo=item
            }

        }
    }
    val id by lazy { arguments?.getInt(ORDER_ID).apply { SLog.info(this.toString()) } }


    companion object {
        @JvmStatic
        fun newInstance(orderid: Int?)=CouponConfirmOrderFragment().apply{
            arguments = Bundle().apply {
                orderid?.let {
                    putInt(ORDER_ID, it)
                }
            }
        }
    }

    override fun initData() {

        EventBus.getDefault().register(this)
        binding.title.apply {
//            setRightLayoutClickListener{ToastUtil.success(context,"分享")}
            setLeftImageResource(R.drawable.icon_back)
            setLeftLayoutClickListener{onBackPressedSupport()}
        }
        binding.rvList.adapter=mAdapter
        id?.let {
            viewModel.getTcBuyStep1(listOf(BuyGoodsDTO(it, 1)))
        }?:hideSoftInputPop()
        binding.btnBuy.setOnClickListener {
            mAdapter.getData()?.let { list ->
                viewModel.getTcBuyStep2(listOf(BuyGoodsDTO(id, 1)))
            }?:SLog.info("没有数据")}
        mAdapter.addAll(listOf(CouponItemVo(null,null,null,null,null,null,null,null,null,null,null,null,null)), true)

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEBMessage(message: EBMessage) {
        when (message.messageType) {
            EBMessageType.MESSAGE_TYPE_COUPON_MPAY_SUCCESS ->{
                viewModel.postMpayNotify()
                Util.startFragment(CouponPayResultFragment.newInstance(Hawk.get(SPField.FIELD_MPAY_PAY_ID),true))
            }
            EBMessageType.MESSAGE_TYPE_COUPON_MPAY_OTHER ->Util.startFragment(CouponPayResultFragment.newInstance(Hawk.get(SPField.FIELD_MPAY_PAY_ID)))
            else ->SLog.info(this::class.java.name)
        }
    }
    override fun initViewObservable() {
        viewModel.buyStep1Vo.observe(this){
            binding.vo=it
            it.couponBaseList?.let { list ->
                mAdapter.addAll(list, false).apply { SLog.info(list.size.toString()) }
            }
        }
        viewModel.buyStep2Vo.observe(this){
            it.orderId?.let {
                viewModel.loadMpay()
            }?:ToastUtil.error(context, "请刷新订单")
        }
        viewModel.mPayVo.observe(this){
            if (it.isPay) {
                MPaySdk.mPayNew(_mActivity, it.toString(), _mActivity as MainActivity)
            }
        }
        viewModel.stateLiveData.stateEnumMutableLiveData.observe(this){
            if (it == StateLiveData.StateEnum.Error) {
                if (Config.USE_DEVELOPER_TEST_DATA) {
                    viewModel.buyStep1Vo.postValue(GsonUtil.gson2Bean(AssetsUtil.loadText(BaseContext.instance.getContext(), "tangram/confirm_order.json"), BuyStep1Vo::class.java))
                }
            }
        }
    }
}

