package com.ftofs.twant.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ftofs.twant.kotlin.net.BaseRepository
import com.ftofs.twant.kotlin.net.Result
import com.ftofs.twant.log.SLog
import com.ftofs.twant.util.ToastUtil
import com.ftofs.twant.util.User
import com.wzq.mvvmsmart.base.BaseViewModelMVVM
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.random.Random

class ImGoodsPageModel(application: Application) :BaseViewModelMVVM(application) {
    val showSearch by lazy {
        MutableLiveData<Boolean>()
    }

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
    val targetName by lazy {
        MutableLiveData<String>()
    }
    var labelId=0
    var page=1
    val net by lazy { object :BaseRepository(){} }

    fun getImGoodsSearch(lableId:String?=null,keyword:String?=null) {
        SLog.info("${searchType}:请求数据")
        targetName.value ="u_007615414781"
        val queryParams = mapOf<String,String>()
        queryParams.plus(User.getTokenPair())
                .plus(this::targetName.run { name to get().value })
                .plus(this::searchType.run { name to get().value })
        lableId?.apply { queryParams.plus("lableId" to this) }
        keyword?.apply { queryParams.plus("keyword" to this) }
        queryParams.forEach((it.))
        viewModelScope.launch(Dispatchers.Default) {
            withContext(Dispatchers.Main){
                val result=net.run { simpleGet(api.getImGoodsSearch(User.getToken(),queryParams)) }
                when(result){
                    is Result.Success -> SLog.info(result.datas.toString())
                    is Result.DataError ->  SLog.info(result.datas.toString())
                    is Result.Error ->  SLog.info(result.exception.toString())
                }
            }
        }
    }
}
