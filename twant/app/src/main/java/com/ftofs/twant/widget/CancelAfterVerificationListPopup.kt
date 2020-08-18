package com.ftofs.twant.widget

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.ftofs.twant.R
import com.ftofs.twant.databinding.PopupCancelAfterVerficationListBinding
import com.ftofs.twant.databinding.VerificationGoodsItemBinding
import com.ftofs.twant.kotlin.adapter.DataBoundAdapter
import com.ftofs.twant.log.SLog
import com.ftofs.twant.vo.orders.OrdersGoodsVo
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.core.CenterPopupView
import com.lxj.xpopup.interfaces.XPopupCallback

class CancelAfterVerificationListPopup(context: Context):CenterPopupView(context), View.OnClickListener{

    private  var orderList: MutableList<OrdersGoodsVo>?=null

    companion object{
        fun newInstance(context: Context, datas: MutableList<OrdersGoodsVo>):CancelAfterVerificationListPopup {
            val args = Bundle()

            val popupView =CancelAfterVerificationListPopup(context)
            popupView.orderList=datas
            return popupView
        }

    }
    override fun onClick(v: View?) {
    }

    override fun onCreate() {
        super.onCreate()
        val binding =DataBindingUtil.findBinding<PopupCancelAfterVerficationListBinding>(this)
        var adapter=object :DataBoundAdapter<OrdersGoodsVo,VerificationGoodsItemBinding>(){
            override val layoutId: Int
                get() = R.layout.verification_goods_item

            override fun initView(binding: VerificationGoodsItemBinding, item: OrdersGoodsVo) {
                SLog.info("暫時這裏也不用幹啥")
                binding.btnCancelAfterVerification.setOnClickListener{
                    XPopup.Builder(context).setPopupCallback(proXcallback()).asCustom(
                        VerificationPopup(context)
                        ).show()
                }
            }


        }
//        adapter.on
        binding?.rvVerificationList?.adapter =adapter

        orderList?.let{
            adapter.addAll(it,true)
        }
    }

    private fun proXcallback(): XPopupCallback {
        return object :XPopupCallback{
            override fun onDismiss() {
                TODO("Not yet implemented")
            }

            override fun onShow() {
                TODO("Not yet implemented")
            }

        }
    }

    override fun getImplLayoutId(): Int {
        return R.layout.popup_cancel_after_verfication_list
    }
}