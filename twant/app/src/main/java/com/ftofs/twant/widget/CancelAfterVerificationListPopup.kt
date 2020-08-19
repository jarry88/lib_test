package com.ftofs.twant.widget

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.ftofs.twant.R
import com.ftofs.twant.databinding.PopupCancelAfterVerficationListBinding
import com.ftofs.twant.databinding.VerificationGoodsItemBinding
import com.ftofs.twant.entity.Goods
import com.ftofs.twant.kotlin.BuyerGoodsListAdapter
import com.ftofs.twant.kotlin.OrderGoodsVoListAdapter
import com.ftofs.twant.kotlin.adapter.DataBoundAdapter
import com.ftofs.twant.log.SLog
import com.ftofs.twant.util.ToastUtil
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
//        val binding =DataBindingUtil.findBinding<PopupCancelAfterVerficationListBinding>(this)
        val binding =PopupCancelAfterVerficationListBinding.inflate(LayoutInflater.from(context))
        var adapter=OrderGoodsVoListAdapter()
//
////        adapter.on
////        binding.rvVerificationList.adapter =adapter
//        binding.adapter=adapter
//
//        orderList?.let{
//            adapter.addAll(it,true)
//        }
//        ToastUtil.success(context,"有幾個${orderList?.size},first ${orderList?.get(0)?.goodsName},item個數 ")
//        val adapter1=BuyerGoodsListAdapter()
//        binding.rvList.adapter=adapter1
//        adapter1.addAll(listOf(Goods(),Goods()),false)
        val rvList=findViewById<RecyclerView>(R.id.rv_verification_list)
        rvList.adapter=adapter
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