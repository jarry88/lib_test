package com.ftofs.twant.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ftofs.lib_net.model.StoreLabel
import com.ftofs.lib_net.model.Goods
import com.ftofs.lib_net.BaseRepository
import com.gzp.lib_common.constant.Result
import com.gzp.lib_common.utils.SLog
import com.ftofs.twant.util.User
import com.wzq.mvvmsmart.base.BaseViewModelMVVM
import com.wzq.mvvmsmart.utils.ToastUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.random.Random

class ImGoodsPageModel(application: Application) :BaseViewModelMVVM(application) {
    var pageNum:Int=1
    var hasMore:Boolean=false
    var isRefresh: Boolean=true

    private val showSearch by lazy {
        MutableLiveData<Boolean>()
    }
    val goodsList by lazy {
        MutableLiveData<ArrayList<Goods>>()
    }
    val storeLabelList by lazy {
        MutableLiveData<ArrayList<StoreLabel>>()
    }
//    val labelEntityList:LiveData<List<MultiItemEntity>> =Transformations.map(storeLabelList){
//        it.map { it.toMultiEntity() }
//    }
    var errorMessage :String?=null

    fun changeSearch() {
        showSearch.value= Random.nextBoolean()
    }
    val searchType by lazy {
        MutableLiveData<String>()
    }//recommend(推薦),history(最近瀏覽),favorite(我的關注),cart(購物袋),owenr(本店商品)
    //labelId keyword page

    val keyword by lazy {
        MutableLiveData<String>()
    }
    val selectLabelId by lazy {
        MutableLiveData<Int>()
    }
    val targetName by lazy {
        MutableLiveData<String>()
    }

    val net by lazy { object : BaseRepository(){} }


    fun getImGoodsSearch(labelId:String?=null, keyword:String?=null,isRefresh :Boolean=true) {
        this.isRefresh=isRefresh
        if (isRefresh) pageNum=1
        val queryParams = mapOf(
                User.getTokenPair(),
                this::targetName.run { name to get().value },
                this::searchType.run { name to get().value }
        ).run {
            plus("page" to (if(isRefresh)pageNum else pageNum+1).toString())
        }.run {
            labelId?.let { plus("labelId" to it )}?:this
        }.run {
            keyword?.let { plus("keyword" to it )}?:this
        }
//        queryParams.forEach{a,b ->SLog.info("$a,$b")}
        SLog.info("${searchType.value}:请求数据  参数[${queryParams}]")

        viewModelScope.launch(Dispatchers.Default) {
            withContext(Dispatchers.Main){
                try {
                    when(val result=net.run { simpleGet(api.getImGoodsSearch(queryParams)) }){
                        is Result.Success -> {
                            if(!isRefresh and !result.datas.goodsList.isNullOrEmpty() )pageNum++
                            goodsList.value=result.datas.goodsList
                            result.datas.storeLabelList?.run {storeLabelList.value=this}
//                            hasMore=result.datas.pageEntity.hasMore
                            stateLiveData.postSuccess()
//                            stateLiveData.postValueAndSuccess(hasMore)

                        }
                        is Result.DataError ->  {
                            errorMessage=result.datas.error
                            stateLiveData.postError()
                        }
                        else ->
                            stateLiveData.postNoNet()
                    }
                }catch (e:Exception){
                    SLog.info("catch exception : ${e.toString()}")
                    stateLiveData.postError()
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
}
