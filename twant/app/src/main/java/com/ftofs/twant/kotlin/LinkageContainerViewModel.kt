package com.ftofs.twant.kotlin

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ftofs.lib_net.model.SellerGoodsItem
import com.gzp.lib_common.base.BaseViewModel
import com.gzp.lib_common.utils.SLog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class LinkageContainerViewModel(application:Application) : BaseViewModel(application){
    var viewModel: LinkageModel = LinkageModel()
    private val _uiState = MutableLiveData<LinkageUiModel>()
    val uiState: LiveData<LinkageUiModel>
        get() = _uiState
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

    //    private val viewModel: LinkageContainerViewModel = LinkageContainerViewModel()
    var pageNum = 1
    //    var liveData: MutableLiveData<MutableList<ItemsEntity?>> = MutableLiveData()
    val liveData: MutableLiveData<ArrayList<SellerGoodsItem>> by lazy {
        MutableLiveData<ArrayList<SellerGoodsItem>>()
    }

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
