package com.ftofs.twant.kotlin

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ftofs.twant.constant.Constant
import com.ftofs.twant.entity.Goods
import com.ftofs.twant.entity.SellerGoodsItem
import com.ftofs.twant.kotlin.base.BaseViewModel
import com.ftofs.twant.kotlin.net.Result
import com.ftofs.twant.log.SLog
import com.ftofs.twant.tangram.SloganView
import com.wzq.mvvmsmart.utils.ToastUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
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
    val currCategoryIndex :MutableLiveData<Int> by lazy {
        MutableLiveData<Int>()
    }
    val nestedScrollingEnable :MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
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
        viewModelScope.launch (Dispatchers.Default){
            withContext(Dispatchers.Main){
                val result = viewModel.getZoneCategoryList(zoneId)
                if (result is Result.Success) {
                    val categoryList = result.datas
                    var a=0
                    var b=-1
                    if(categoryList.zoneGoodsCategoryList.isEmpty()){
                        currCategoryId.value=categoryList.checkedCategory
                        return@withContext
                    }
                    categoryList.zoneGoodsCategoryList.forEach outside@{ it ->
                        if (it.equals(categoryList.checkedCategory)) {
                            return@outside
                        }
                        b=-1
                        it.nextList.forEach inside@{sub->
                            if (sub.equals(categoryList.checkedCategory)) {
                                    b++
                                return@outside
                            }
                            b++
                        }
                        a++
                    }
                    if (a >= categoryList.zoneGoodsCategoryList.size) {
                        a=0
                    }
                    categoryData.value = categoryList.zoneGoodsCategoryList

                    delayClick(a,b)
                    stateLiveData.postIdle()
                } else {
                    stateLiveData.postNoData()
                }
            }
        }
    }

    private fun delayClick(item:Int, subIndex:Int) {
        viewModelScope.launch {
            delay(200)
            currCategoryIndex.value=item//添加默认列表Id
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
    fun doGetZoneGoodsItems() {
        viewModelScope.launch (Dispatchers.Default){
            val categoryId = currCategoryId.value
            categoryId?:stateLiveData.postError()
            categoryId?.let {
                SLog.info("执行加载商品列表数据$it")
                withContext(Dispatchers.Main){
                    val result = viewModel.getShoppingZoneGoods(it, pageNum)
                    SLog.info(result.toString())
                    if (result is Result.Success) {
                        result.datas.zoneGoodsList?.let {

                            ToastUtils.showShort("拿到數據了")

                            goodsList.value = result.datas.zoneGoodsList
                            stateLiveData.postSuccess()
                        }?:let{
                            ToastUtils.showShort("拿到數據爲空")
                            if (pageNum <= 1) {
                                //表示執行下拉刷新
                                stateLiveData.postNoData()
                            }else{
                                stateLiveData.postNoMoreData()
                                pageNum--
                                ToastUtils.showShort("沒有更多數據了")
                            }
                        }

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
