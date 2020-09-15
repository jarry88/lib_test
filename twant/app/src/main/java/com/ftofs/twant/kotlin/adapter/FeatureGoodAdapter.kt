package com.ftofs.twant.kotlin.adapter

//import com.ftofs.twant.databinding.SellerGoodsItemBinding
import android.util.Log
import android.widget.ImageView
import cn.snailpad.easyjson.EasyJSONObject
import com.bumptech.glide.Glide
import com.ftofs.twant.R
import com.ftofs.twant.api.Api
import com.ftofs.twant.api.UICallback
import com.ftofs.twant.constant.Constant
import com.ftofs.twant.databinding.SellerGoodsItemUnswipeBinding
import com.ftofs.lib_net.model.SellerGoodsItem
import com.ftofs.lib_net.model.SellerGoodsVO
import com.gzp.lib_common.utils.SLog
import com.ftofs.twant.seller.fragment.SellerGoodsDetailFragment
import com.ftofs.twant.seller.fragment.SellerGoodsSkuListFragment
import com.ftofs.twant.util.LogUtil
import com.ftofs.twant.util.ToastUtil
import com.ftofs.twant.util.User
import com.ftofs.twant.util.Util
import okhttp3.Call
import java.io.IOException

class FeatureGoodAdapter : DataBoundAdapter<SellerGoodsVO, SellerGoodsItemUnswipeBinding>() {
//    private val dao = AppDataBase.get().historyDao()
//    var username = SP.getString(SP.KEY_USER_NAME)
    override val layoutId: Int
        get() = R.layout.seller_goods_item_unswipe

    override fun initView(binding: SellerGoodsItemUnswipeBinding, item: SellerGoodsVO) {
        binding.vo = SellerGoodsItem()
        if (item.goodsModal == Constant.GOODS_TYPE_CONSULT) {
            binding.tvPriceRange.text = "詢價"
        }
        binding.btnMore.setOnClickListener{
                    var params = EasyJSONObject.generate("token", User.getToken(), "commonId", item.commonId)
                    val loadingPopup = Util.createLoadingPopup(context).show()
            var path=Api.SELLER_GOODS_FEATURES
            if (item.isCommend==2) path=Api.SELLER_CANCEL_FEATURES
            SLog.info("params[%s]", params)

            Api.postUI(path, params, object : UICallback() {
                override fun onFailure(call: Call, e: IOException) {
                    LogUtil.uploadAppLog(path, params.toString(), "", e.message);
                    loadingPopup.dismiss()
                    ToastUtil.showNetworkError(context, e)
                }

                @Throws(IOException::class)
                override fun onResponse(call: Call, responseStr: String) {
                    loadingPopup.dismiss()
                    try {
                        SLog.info("responseStr[%s]", responseStr)
                        val responseObj = EasyJSONObject.parse<EasyJSONObject>(responseStr)
                        if (ToastUtil.checkError(context, responseObj)) {
                            LogUtil.uploadAppLog(path, params.toString(), responseStr, "");
                            return
                        }
                        when(item.isCommend){
                            2 ->{
                                item.isCommend = 0
                                binding.btnMore.setImageDrawable(R.drawable.icon_add_blue)

                            }
                            else ->{
                                item.isCommend = 2
                                binding.btnMore.setImageDrawable(R.drawable.ic_blue_mium)

                            }

                        }

//                        changeItemCommend(item)
//                        notifyItemChanged(p)
                    } catch (e: Exception) {
                        SLog.info("Error!message[%s], trace[%s]", e.message, Log.getStackTraceString(e))
                    }
                }
            })
        }
        binding.btnViewAllSku.setOnClickListener{
            Util.startFragment(SellerGoodsSkuListFragment.newInstance(item.commonId))
        }
        binding.root.setOnClickListener{
            Util.startFragment(SellerGoodsDetailFragment.newInstance(item.commonId, item.imageName))
        }
    }


}


//        binding.root.setOnClickListener {
//            RouterUtil.navWeb(item, it.context)
//        }


private fun ImageView.setImageDrawable(id: Int) {
    Glide.with(this).load(id).centerInside().into(this)
}
