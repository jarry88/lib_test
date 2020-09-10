package com.ftofs.twant.kotlin

import com.ftofs.twant.R
import com.ftofs.twant.databinding.VerificationGoodsItemBinding
import com.ftofs.twant.entity.cart.SpuStatus
import com.ftofs.twant.kotlin.adapter.DataBoundAdapter
import com.ftofs.twant.vo.orders.OrdersGoodsVo
import com.ftofs.twant.widget.CancelAfterVerificationListPopup

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
            parent.showVerificationPopup(item,binding.abQuantity.value)
        }
        val spuStatus=SpuStatus()
        spuStatus.goodsId=item.goodsId
        binding.abQuantity.setMinValue(1, null);  // 調節數量不能小于1
        binding.abQuantity.setMaxValue(item.ifoodmacauCount,null)
        binding.abQuantity.setSpuStatus(spuStatus)
        binding.abQuantity.value=item.ifoodmacauCount
    }

    fun clear() {
        mData.clear()
    }
}