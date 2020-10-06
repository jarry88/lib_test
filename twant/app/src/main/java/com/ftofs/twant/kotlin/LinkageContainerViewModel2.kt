package com.ftofs.twant.kotlin

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ftofs.twant.config.Config
import com.ftofs.lib_net.model.Goods
import com.ftofs.lib_net.model.SellerGoodsItem
import com.ftofs.lib_net.model.ZoneCategory
import com.gzp.lib_common.base.BaseViewModel
import com.gzp.lib_common.constant.Result
import com.gzp.lib_common.utils.SLog
import com.gzp.lib_common.smart.utils.ToastUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class LinkageContainerViewModel2(application:Application) : BaseViewModel(application){
    var isRefresh: Boolean=false
    var viewModel: LinkageModel = LinkageModel()
    private val _uiState = MutableLiveData<LinkageUiModel>()

    val liveData: MutableLiveData<ArrayList<SellerGoodsItem>> by lazy {
        MutableLiveData<ArrayList<SellerGoodsItem>>()
    }
    val currCategoryId :MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    val currCategoryIndex :MutableLiveData<Int> by lazy {
        MutableLiveData<Int>()
    }
    val nestedScrollingEnable :MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }
    val categoryData: MutableLiveData<List<ZoneCategory>> by lazy {
        MutableLiveData<List<ZoneCategory>>()
    }
    val goodsList: MutableLiveData<ArrayList<Goods>> by lazy {
        MutableLiveData<ArrayList<Goods>>()
    }
    val uiState: LiveData<LinkageUiModel>
        get() = _uiState
    fun getZoneCategoryList(zoneId: Int){
        viewModelScope.launch (Dispatchers.Default){
            withContext(Dispatchers.Main){
                val result = viewModel.getZoneCategoryList(zoneId)
                if (result is Result.Success) {
                    val categoryList = result.datas.zoneGoodsCategoryList
                    val checkedCategory =result.datas.checkedCategory
                    var a=0
                    var b=-1
                    if (categoryList == null) {
                        currCategoryId.value=checkedCategory
                    }
                    categoryList?.let {
                        it.forEach()outside@{ it ->
                            if (it.equals(checkedCategory)) {
                                return@outside
                            }
                            b=-1
                            it.nextList.forEach inside@{sub->
                                if (sub.equals(checkedCategory)) {
                                    b++
                                    return@outside
                                }
                                b++
                            }
                            a++
                        }
                        if (a >= it.size) {
                            a=0
                        }
                        categoryData.value = it

                        delayClick(a)
                        stateLiveData.postIdle()

                    }

                } else {
                    stateLiveData.postNoData()
                }
            }
        }
    }

    fun delayClick(item:Int) {
        viewModelScope.launch {
            delay(150)
            currCategoryIndex.value=item//添加默认列表Id
        }
    }

    fun doGetGoodsItems(zoneId:Int) {
//        viewModelScope.launch (Dispatchers.Default){
//            withContext(Dispatchers.Main){
////                val result = viewModel.getShoppingGoodsList(zoneId)
//                val result = viewModel.getShoppingGoodsList1(zoneId)
//                SLog.info(result.toString())
//            }
//        }
        launch(stateLiveData,
                {viewModel.getShoppingGoodsList1(zoneId)},
                {SLog.info(it.toString())}
        )
    }
    fun doGetZoneGoodsItems() {
        viewModelScope.launch (Dispatchers.Default){
            val categoryId = currCategoryId.value
            categoryId?:stateLiveData.postError()
            categoryId?.let {
                SLog.info("执行加载商品列表数据$it")
                withContext(Dispatchers.Main){
                    if(isRefresh) pageNum=1
                    else pageNum++

                    val result = viewModel.getShoppingZoneGoods(it, pageNum)
                    if (result is Result.Success) {
                        val list =result.datas.zoneGoodsList
                        if (list == null || list.size == 0) {
                            if (isRefresh)stateLiveData.postNoData()
                            else{
                                pageNum--
                                stateLiveData.postNoMoreData()
                            }
                        }else{
                            if (Config.USE_DEVELOPER_TEST_DATA) {
                                list.forEach{ SLog.info("[%s]", it.goodsName+it.jingle)}
                            }
                            goodsList.value = result.datas.zoneGoodsList
                            stateLiveData.postSuccess()

                        }
                        hasMore=result.datas.pageEntity.hasMore

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
