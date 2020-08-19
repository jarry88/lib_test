package com.ftofs.twant.kotlin

import com.ftofs.twant.R
import com.ftofs.twant.databinding.VerificationGoodsItemBinding
import com.ftofs.twant.kotlin.adapter.DataBoundAdapter
import com.ftofs.twant.log.SLog
import com.ftofs.twant.util.ToastUtil
import com.ftofs.twant.vo.orders.OrdersGoodsVo
import com.ftofs.twant.widget.VerificationPopup
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.interfaces.XPopupCallback

/**
 * author : 谷志鹏
 * date   : 2020/07/09 11:34
 */
class OrderGoodsVoListAdapter: DataBoundAdapter<OrdersGoodsVo, VerificationGoodsItemBinding>() {
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
                    .asCustom(VerificationPopup(context,item))
                    .show()
        }
//        binding.root.setOnClickListener{
//            Util.startFragment(GoodsDetailFragment.newInstance(item.commonId, 0))
//        }
    }

    private fun reloadData() {
        ToastUtil.success(context,"重新加載數據")
    }

    fun clear() {
        mData.clear()
    }
}