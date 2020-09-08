package com.ftofs.twant.kotlin

import android.util.Log
import cn.snailpad.easyjson.EasyJSONObject
import com.ftofs.twant.R
import com.ftofs.twant.api.Api
import com.ftofs.twant.api.UICallback
import com.ftofs.twant.databinding.VerificationGoodsItemBinding
import com.ftofs.twant.entity.cart.SpuStatus
import com.ftofs.twant.kotlin.adapter.DataBoundAdapter
import com.ftofs.twant.log.SLog
import com.ftofs.twant.util.ToastUtil
import com.ftofs.twant.util.User
import com.ftofs.twant.util.Util
import com.ftofs.twant.vo.orders.OrdersGoodsVo
import com.ftofs.twant.widget.CancelAfterVerificationListPopup
import com.ftofs.twant.widget.VerificationPopup
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.interfaces.XPopupCallback
import okhttp3.Call
import java.io.IOException
import java.util.ArrayList

/**
 * author : 谷志鹏
 * date   : 2020/07/09 11:34
 */
class OrderGoodsVoListAdapter(val parent:CancelAfterVerificationListPopup): DataBoundAdapter<OrdersGoodsVo, VerificationGoodsItemBinding>() {
//        helper.setText(R.id.iv2, item.news_title)
override val layoutId: Int
    get()=R.layout.verification_goods_item

    override fun initView(binding: VerificationGoodsItemBinding, item: OrdersGoodsVo) {
        binding.vo = item
        binding.btnCancelAfterVerification.setOnClickListener{
            XPopup.Builder(context).
            moveUpToKeyboard(false)
                    .setPopupCallback(
                           object :XPopupCallback{
                               override fun onDismiss() {
                                   reloadData()
                               }

                               override fun onShow() {
//                                   TODO("Not yet implemented")
                               }

                           }
                    )
                    .asCustom(VerificationPopup(context,item,binding.abQuantity.value))
                    .show()
        }
        val spuStatus=SpuStatus()
        spuStatus.goodsId=item.goodsId
        binding.abQuantity.setMinValue(1, null);  // 調節數量不能小于1
        binding.abQuantity.setMaxValue(item.ifoodmacauCount,null)
        binding.abQuantity.setSpuStatus(spuStatus)
        binding.abQuantity.value=item.ifoodmacauCount
    }

    private fun reloadData() {
       parent.reloadata()
    }

    fun clear() {
        mData.clear()
    }
}