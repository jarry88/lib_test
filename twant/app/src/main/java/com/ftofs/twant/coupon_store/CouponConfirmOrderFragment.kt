package com.ftofs.twant.coupon_store

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import cn.snailpad.easyjson.EasyJSONObject
import com.ftofs.lib_net.model.*
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
import com.ftofs.twant.dsl.invisible
import com.ftofs.twant.entity.EBMessage
import com.ftofs.twant.interfaces.SimpleCallback
import com.ftofs.twant.kotlin.setVisibleOrGone
import com.ftofs.twant.util.AssetsUtil
import com.ftofs.twant.util.ToastUtil
import com.ftofs.twant.util.User
import com.ftofs.twant.util.Util
import com.ftofs.twant.widget.AdjustButton
import com.gzp.lib_common.base.BaseTwantFragmentMVVM
import com.gzp.lib_common.utils.BaseContext
import com.gzp.lib_common.utils.SLog
import com.lxj.xpopup.core.BasePopupView
import com.macau.pay.sdk.MPaySdk
import com.orhanobut.hawk.Hawk
import com.wzq.mvvmsmart.event.StateLiveData
import kotlinx.android.synthetic.main.coupon_list_item_wighet.view.*
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
    var showSuccess = false

    var mLoading :BasePopupView?=null
    var currAb: AdjustButton?=null
    var delayValue :Int?=null
    val mAdapter by lazy {
        factoryAdapter<CouponItemVo, CouponOrderConfirmItemBinding>(R.layout.coupon_order_confirm_item){ b, d ->
            b.vo=d
            b.couponListItem.mBinding.apply {
                tvEndPrice.setVisibleOrGone(true)
                tvEndPrice.text =d.price.toString()
                tvSubTitle.setVisibleOrGone(true)
            }
//            b.fixed.setFixedText("留言：")

            if("留言：" != b.fixed.fixedText){
                b.fixed.fixedText = "留言："
            }
            b.fixed.doAfterTextChanged {
                viewModel.remark=it.toString()
            }
            b.fixed.isFocusable=true
            b.abQuantity.apply {
                //            b.fixed.setFixedText("留言：")
                currAb=this

                setMinValue(1){}
                mValueChangeListener= SimpleCallback { value ->
                    delayValue =value as Int
                    mLoading= Util.createLoadingPopup(_mActivity).show()
                    viewModel.getTcBuyStep1(listOf(BuyGoodsDTO(d.id, delayValue)))
                }
                d.limitStock?.let {
                    if (it) {
                        setMaxValue(d.stock.apply { SLog.info("limit ${d.limitStock} stock: $this,limitBuy ${d.limitBuyNum}") } ?: Int.MAX_VALUE){
                            SLog.info("已經達到上限")
                        }
                    }
                }
                if (value <= 0) {
                    value=1
                }
                d.num=value
                if (value== 0) {
                    binding.tvPrice.text="-"
//                    viewModel.getTcBuyStep1(listOf(BuyGoodsDTO(d.id, 0)))
                }
            }

        }
    }
    val id by lazy { arguments?.getInt(ORDER_ID).apply { SLog.info(this.toString()) } }


    companion object {
        @JvmStatic
        fun newInstance(couponid: Int?)=CouponConfirmOrderFragment().apply{
            arguments = Bundle().apply {
                couponid?.let {
                    putInt(ORDER_ID, it).apply { SLog.info("Coupon_ID :$it") }
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EventBus.getDefault().register(this)

    }

    override fun initData() {

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
            if (User.getUserId() > 0) {
                mLoading =Util.createLoadingPopup(_mActivity).show()
                mAdapter.getData()?.let { list ->
                    currAb?.isFocusable=true
                    viewModel.getTcBuyStep2(listOf(BuyGoodsDTO(id, currAb?.value?:0).apply {
                        SLog.info(viewModel.buyStep1Vo.value.toString())
                    }))
                }?:SLog.info("没有数据")}
               else {Util.showLoginFragment(requireContext())
            }
        }
    }

    override fun onSupportVisible() {
        super.onSupportVisible()
        if (showSuccess) {
           showResultFragment()
        }
        currAb?.isFocusable =true
    }

    override fun onSupportInvisible() {
        super.onSupportInvisible()
        showSuccess=false
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEBMessage(message: EBMessage) {
        when (message.messageType) {
            EBMessageType.MESSAGE_TYPE_COUPON_MPAY_SUCCESS -> {

//                viewModel.postMpayNotify()
                if (isSupportVisible) {
                    showResultFragment()
                } else {
                    showSuccess= true
                }
            }
            EBMessageType.COUPON_CANCEL_SHOW_RESULT_SUCCESS -> showSuccess=false

            EBMessageType.MESSAGE_TYPE_COUPON_MPAY_OTHER ->if (isSupportVisible) {
                SLog.info("支付失败")
//                    Util.startFragment(CouponPayResultFragment.newInstance(Hawk.get(SPField.FIELD_MPAY_PAY_ID)))
            }
            else ->{//SLog.info(this::class.java.name)
            }
        }
    }

    private fun showResultFragment() {
        EBMessage.postMessage(EBMessageType.COUPON_CANCEL_SHOW_RESULT_SUCCESS,null)

        viewModel.buyStep2Vo.value?.let {
            Util.startFragment(CouponPayResultFragment.newInstance(
                    it.orderId //Hawk.get(SPField.FIELD_MPAY_PAY_ID)
                    , true)).apply { SLog.info("由 ${this.javaClass.name}拉起") }
        }
    }

    override fun initViewObservable() {
        viewModel.buyStep1Vo.observe(this){
            binding.vo=it
            mLoading?.dismiss()

            it.couponBaseList?.let { list ->
                mAdapter.addAll(list, true).apply { SLog.info(list.size.toString()) }
            }
            delayValue?.let {v ->
                currAb?.value=v
            }

        }
        viewModel.buyStep2Vo.observe(this){
            SLog.info("返回成功")
            it.orderId?.let {
                viewModel.loadMpay()
            }?:ToastUtil.error(context, "请刷新订单")
        }
        viewModel.mPayVo.observe(this){
            if (it.isPay) {
                ToastUtil.success(context,"已經支付過了")
            } else {
                MPaySdk.mPayNew(_mActivity, it.payData.toString().apply { SLog.info(this) }, _mActivity as MainActivity)
            }
        }
        viewModel.stateLiveData.stateEnumMutableLiveData.observe(this){
            mLoading?.dismiss()
            if (it == StateLiveData.StateEnum.Error) {
                if (Config.USE_DEVELOPER_TEST_DATA) {
                    viewModel.buyStep1Vo.postValue(GsonUtil.gson2Bean(AssetsUtil.loadText(BaseContext.instance.getContext(), "tangram/confirm_order.json"), BuyStep1Vo::class.java))
                }
            }
        }
    }
}

