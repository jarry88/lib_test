package com.ftofs.twant.kotlin

import com.ftofs.twant.entity.SellerGoodsItem
import com.gzp.lib_common.net.MRequest
import com.ftofs.twant.kotlin.vo.SellerPageVO
import com.wzq.mvvmsmart.base.BaseModelMVVM
import com.wzq.mvvmsmart.net.base.BaseResponse
import io.reactivex.Observable

/**
 * 作者：王志强
 * 创建时间：2020/4/13
 * 文件描述：
 */
class FeatureGoodModel : BaseModelMVVM() {
    /**
     * 数据来自内存
     */
//    fun doGetServerNews1(pageNum: Int): Observable<BaseResponse<ArrayList<SellerGoodsItem>>> {
//        return MRequest.getInstance().doGetServerNews(pageNum)
//    }
//
//    /**
//     * 数据来自DB
//     */
//    fun doGetServerNews2(pageNum: Int): Observable<BaseResponse<ArrayList<SellerGoodsItem>>> {
//        return MRequest.getInstance().doGetServerNews(pageNum)
//    }

    /**
     * 数据来自网络
     */
//    fun doGetServerNews(pageNum: Int): Observable<BaseResponse<ArrayList<SellerGoodsItem>>> {
//        return MRequest.getInstance().doGetServerNews(pageNum)
//    }
    /**
     * 数据来自网络
     */
    fun doSellerGoodsList(params:@JvmSuppressWildcards Map<String ,Any>): Observable<BaseResponse<SellerPageVO<SellerGoodsItem>>> {

        return MRequest.getInstance().doSellerGoodsList(params)
    }

}