package com.ftofs.twant.kotlin

import android.util.Log
import androidx.appcompat.widget.DrawableUtils
import cn.snailpad.easyjson.EasyJSONObject
import com.ftofs.twant.R
import com.ftofs.twant.api.Api
import com.ftofs.twant.api.UICallback
import com.ftofs.twant.databinding.SellerGoodsItemUnswipeBinding
import com.ftofs.twant.databinding.ZoneGoodsListItemBinding
import com.ftofs.twant.entity.Goods
import com.ftofs.twant.entity.SellerGoodsItem
import com.ftofs.twant.fragment.GoodsDetailFragment
import com.ftofs.twant.kotlin.adapter.DataBoundAdapter
import com.ftofs.twant.log.SLog
import com.ftofs.twant.seller.fragment.SellerGoodsDetailFragment
import com.ftofs.twant.seller.fragment.SellerGoodsSkuListFragment
import com.ftofs.twant.util.ToastUtil
import com.ftofs.twant.util.User
import com.ftofs.twant.util.Util
import okhttp3.Call
import java.io.IOException

/**
 * author : 谷志鹏
 * date   : 2020/07/09 11:34
 */
class BuyerGoodsListAdapter: DataBoundAdapter<Goods, ZoneGoodsListItemBinding>() {
//        helper.setText(R.id.iv2, item.news_title)
override val layoutId: Int
    get()=R.layout.zone_goods_list_item

    override fun initView(binding: ZoneGoodsListItemBinding, item: Goods) {
        binding.vo = item
//        binding.btnMore.setOnClickListener{
//            var params = EasyJSONObject.generate("token", User.getToken(), "commonId", item.commonId)
//            val loadingPopup = Util.createLoadingPopup(context).show()
//            var path= Api.SELLER_GOODS_FEATURES
//            if (item.isCommend==2) path= Api.SELLER_CANCEL_FEATURES
//            SLog.info("params[%s]", params)
//
//            Api.postUI(path, params, object : UICallback() {
//                override fun onFailure(call: Call, e: IOException) {
//                    loadingPopup.dismiss()
//                    ToastUtil.showNetworkError(context, e)
//                }
//
//                @Throws(IOException::class)
//                override fun onResponse(call: Call, responseStr: String) {
//                    loadingPopup.dismiss()
//                    try {
//                        SLog.info("responseStr[%s]", responseStr)
//                        val responseObj = EasyJSONObject.parse<EasyJSONObject>(responseStr)
//                        if (ToastUtil.checkError(context, responseObj)) {
//                            return
//                        }
//                        when(item.isCommend){
//                            2 ->{
//                                item.isCommend = 0
//
//                            }
//                            else ->{
//                                item.isCommend = 2
//
//                            }
//
//                        }
//
////                        changeItemCommend(item)
////                        notifyItemChanged(p)
//                    } catch (e: Exception) {
//                        SLog.info("Error!message[%s], trace[%s]", e.message, Log.getStackTraceString(e))
//                    }
//                }
//            })
//        }
//        binding.btnViewAllSku.setOnClickListener{
//            Util.startFragment(SellerGoodsSkuListFragment.newInstance(item.commonId))
//        }
        binding.root.setOnClickListener{
            Util.startFragment(GoodsDetailFragment.newInstance(item.commonId, 0))
        }
    }

    fun clear() {
        mData.clear()
    }
}