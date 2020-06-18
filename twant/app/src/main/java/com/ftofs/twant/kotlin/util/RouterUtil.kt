package com.ftofs.twant.kotlin.util

import android.content.Context
import android.os.Bundle
import android.provider.SyncStateContract
import com.ftofs.twant.entity.SellerGoodsItem
import io.github.iamyours.router.ARouter

object RouterUtil {
    @JvmOverloads
    fun navWeb(
        item: SellerGoodsItem,
        context: Context,
        callback: ((Boolean) -> Unit)? = null //回调
    ) {
        val bundle = Bundle()
        bundle.putInt("commonId", item.commonId)
        bundle.putInt("isCommend", item.isCommend)
        bundle.putString("link", item.imageName)
        ARouter.getInstance().build("/web")
            .with(bundle)
            .navigation(context) { _, resultCode, data ->
                if (resultCode == Constants.RESULT_COLLECT_CHANGED) {
                    item.isCommend = data.getIntExtra("isCommend", 0)
                    callback?.invoke(item.isCommend==1)
                }
            }
    }

//    @JvmOverloads
//    fun navWeb(
//        item: CacheArticleVO,
//        context: Context,
//        callback: ((Boolean) -> Unit)? = null //回调
//    ) {
//        val bundle = Bundle()
//        bundle.putString("link", item.link)
//        ARouter.getInstance().build("/web")
//            .with(bundle)
//            .navigation(context)
//    }
}