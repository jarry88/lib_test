package com.ftofs.twant.kotlin

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ftofs.twant.entity.SellerGoodsItem
import com.ftofs.twant.kotlin.base.BaseViewModel
import com.ftofs.twant.log.SLog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class LinkageContainerViewModel2(application:Application) :BaseViewModel(application){
    var viewModel: LinkageModel = LinkageModel()
    private val _uiState = MutableLiveData<LinkageUiModel>()

    val liveData: MutableLiveData<ArrayList<SellerGoodsItem>> by lazy {
        MutableLiveData<ArrayList<SellerGoodsItem>>()
    }
    val categoryData: MutableLiveData<ArrayList<ZoneCategory>> by lazy {
        MutableLiveData<ArrayList<ZoneCategory>>()
    }
    val uiState: LiveData<LinkageUiModel>
        get() = _uiState
    fun getShoppingZone(zoneId: Int){
        viewModelScope.launch (Dispatchers.Default){
            withContext(Dispatchers.Main){
                val result = viewModel.getShoppingZone(zoneId)
            }
        }
    }
    fun doGetGoodsItems(zoneId:Int) {
        viewModelScope.launch (Dispatchers.Default){
            withContext(Dispatchers.Main){
//                val result = viewModel.getShoppingGoodsList(zoneId)
                val result = viewModel.getShoppingGoodsList1(zoneId)
                SLog.info(result.toString())
            }
        }
        SLog.info("执行数据")
    }
    fun doGetZoneGoodsItems(zoneId:Int) {
        SLog.info("执行請求数据")
        val item = SellerGoodsItem()
        when (liveData.value?.size?.let { it%2 }){
            0 -> item.goodsName="張三"
            1 -> item.goodsName="零零"
            else -> item.goodsName="李思思"
        }
        liveData.value?.let { it.add(item) }?:let { liveData.value= arrayListOf(item) }
        SLog.info("[%d] [%s]",liveData.value?.size,item.goodsName)
        liveData.value =liveData.value

    }

    //    private val viewModel: LinkageContainerViewModel = LinkageContainerViewModel()
    var pageNum = 1
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
