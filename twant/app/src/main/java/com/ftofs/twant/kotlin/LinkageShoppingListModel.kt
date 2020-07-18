package com.ftofs.twant.kotlin

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.ftofs.twant.entity.SellerGoodsItem
import com.ftofs.twant.entity.StoreItem
import com.ftofs.twant.kotlin.base.BaseViewModel
import com.ftofs.twant.kotlin.net.Result
import com.ftofs.twant.log.SLog
import com.wzq.mvvmsmart.utils.ToastUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class LinkageShoppingListModel(application:Application) :BaseViewModel(application){
    var viewModel: LinkageModel = LinkageModel()
    private val _uiState = MutableLiveData<LinkageUiModel>()

    val liveData: MutableLiveData<ArrayList<SellerGoodsItem>> by lazy {
        MutableLiveData<ArrayList<SellerGoodsItem>>()
    }

    val nestedScrollingEnable :MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    val storesList: MutableLiveData<ArrayList<StoreItem>> by lazy {
        MutableLiveData<ArrayList<StoreItem>>()
    }


    fun doGetStoreItems(zoneId: Int) {
        MainScope().launch (Dispatchers.Default){
            withContext(Dispatchers.Main){
                val result=viewModel.getZoneStoreList(zoneId,pageNum)
                if (result is Result.Success) {
                    result.datas.zoneStoreList?.let {
                        if (it.size == 0) nullDeal()

//                            goodsList.value
//                            goodsList.value?.let {
//                                it.addAll(result.datas.zoneGoodsList!!)
//                            }?:let{
                        storesList.value = result.datas.zoneStoreList
//                            }
                        stateLiveData.postSuccess()
                    }?:nullDeal()
                    hasMore=result.datas.pageEntity.hasMore
                }else if (result is Result.DataError) {
                    SLog.info(result.datas.error)
                    ToastUtils.showShort(result.datas.error)
                    stateLiveData.postError()
                } else {
                    stateLiveData.postNoData()
                }
            }

        }
    }

    private fun nullDeal() {
        if (pageNum <= 1) {
            //表示執行下拉刷新
            stateLiveData.postNoData()
        }else{
            stateLiveData.postNoMoreData()
            pageNum--
            ToastUtils.showShort("沒有更多數據了")
        }
    }

    //    private val viewModel: LinkageContainerViewModel = LinkageContainerViewModel()
    var pageNum:Int=1
    var hasMore:Boolean=false
    //    var liveData: MutableLiveData<MutableList<ItemsEntity?>> = MutableLiveData()

    data class LinkageUiModel(
            val showHot: Boolean,
            val showLoading: Boolean,
            val showError: String?,
//            val showSuccess: ArticleList?,
            val showEnd: Boolean, // 加载更多
            val isRefresh: Boolean, // 刷新
            val showWebSites: List<SellerGoodsItem>?,
            val showHotSearch: List<SellerGoodsItem>?
    )
}
