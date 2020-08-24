package com.ftofs.twant.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ftofs.twant.domain.store.StoreLabel
import com.ftofs.twant.entity.ImStoreOrderItem
import com.ftofs.twant.kotlin.net.BaseRepository
import com.ftofs.twant.kotlin.net.Result
import com.ftofs.twant.log.SLog
import com.ftofs.twant.util.User
import com.wzq.mvvmsmart.base.BaseViewModelMVVM
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.random.Random

class ImOrdersPageModel(application: Application) :BaseViewModelMVVM(application) {
    var pageNum:Int=1
    var hasMore:Boolean=false
    var isRefresh: Boolean=true

    private val showSearch by lazy {
        MutableLiveData<Boolean>()
    }
    val ordersList by lazy {
        MutableLiveData<ArrayList<ImStoreOrderItem>>()
    }
    val storeLabelList by lazy {
        MutableLiveData<ArrayList<StoreLabel>>()
    }
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
    val imName by lazy {
        MutableLiveData<String>()
    }

    val net by lazy { object :BaseRepository(){} }


    fun getImOrdersSearch( keyword:String?=null,isRefresh :Boolean=true) {
        this.isRefresh=isRefresh
        if (isRefresh) pageNum=1
        val queryParams = mapOf(
                User.getTokenPair(),
                this::imName.run { name to get().value }
        ).run {
            plus("page" to (if(isRefresh)pageNum else pageNum+1).toString())
        }.run {
            keyword?.let { plus("keyword" to it )}?:this
        }
//        queryParams.forEach{a,b ->SLog.info("$a,$b")}
        SLog.info("请求数据  参数[${queryParams}]")

        viewModelScope.launch(Dispatchers.Default) {
            withContext(Dispatchers.Main){
                try {
                    when(val result=net.run { simpleGet(api.getImOrdersSearch(queryParams)) }){
                        is Result.Success -> {
                            if(!isRefresh and !result.datas.ordersList.isNullOrEmpty() )pageNum++
                            ordersList.value=result.datas.ordersList
//                            hasMore=result.datas.pageEntity.hasMore
                            stateLiveData.postSuccess()

                        }
                        is Result.DataError ->  {
                            errorMessage=result.datas.error
                            stateLiveData.postError()
                        }
                        else ->
                            stateLiveData.postNoNet()
                    }
                }catch (e:Exception){
                    SLog.info("catch exception : $e")
                    stateLiveData.postError()
                }

            }
        }
    }
}
