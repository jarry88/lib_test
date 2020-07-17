package com.ftofs.twant.kotlin

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ftofs.twant.entity.Goods
import com.ftofs.twant.entity.SellerGoodsItem
import com.ftofs.twant.kotlin.base.BaseViewModel
import com.ftofs.twant.kotlin.net.Result
import com.ftofs.twant.log.SLog
import com.wzq.mvvmsmart.utils.ToastUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class LinkageContainerViewModel2(application:Application) :BaseViewModel(application){
    var viewModel: LinkageModel = LinkageModel()
    private val _uiState = MutableLiveData<LinkageUiModel>()

    val liveData: MutableLiveData<ArrayList<SellerGoodsItem>> by lazy {
        MutableLiveData<ArrayList<SellerGoodsItem>>()
    }
    val currCategoryId :MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    val categoryData: MutableLiveData<List<ZoneCategory>> by lazy {
        MutableLiveData<List<ZoneCategory>>()
    }
    val goodsList: MutableLiveData<List<Goods>> by lazy {
        MutableLiveData<List<Goods>>()
    }
    val uiState: LiveData<LinkageUiModel>
        get() = _uiState
    fun getZoneCategoryList(zoneId: Int){
        SLog.info("执行請求数据")
        viewModelScope.launch (Dispatchers.Default){
            withContext(Dispatchers.Main){
                val result = viewModel.getZoneCategoryList(zoneId)
                if (result is Result.Success) {
                    SLog.info("拿到數據")
                    val categoryList = result.datas
                    categoryData.value = categoryList.zoneGoodsCategoryList
                    stateLiveData.postSuccess()
                } else {
                    stateLiveData.postError()
                }
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
    }
    fun doGetZoneGoodsItems(categoryId:String) {
        viewModelScope.launch (Dispatchers.Default){
            withContext(Dispatchers.Main){
                val result = viewModel.getShoppingZoneGoods(categoryId, pageNum)
                SLog.info(result.toString())
                if (result is Result.Success) {
                    goodsList.value = result.datas.zoneGoodsList
                    stateLiveData.postSuccess()

                } else if (result is Result.DataError) {
                    SLog.info(result.datas.error)
                    ToastUtils.showShort(result.datas.error)
                    stateLiveData.postError()
                } else {
                    stateLiveData.postNoData()
                }
            }
        }
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
