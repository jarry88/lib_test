package com.ftofs.twant.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.ftofs.twant.domain.store.StoreLabel
import com.ftofs.twant.entity.Goods
import com.ftofs.twant.kotlin.net.BaseRepository
import com.ftofs.twant.kotlin.net.Result
import com.ftofs.twant.kotlin.net.TwantService
import com.ftofs.twant.log.SLog
import com.ftofs.twant.util.ToastUtil
import com.ftofs.twant.util.User
import com.ftofs.twant.vo.goods.GoodsVo
import com.wzq.mvvmsmart.base.BaseViewModelMVVM
import kotlinx.android.synthetic.main.date_picker_view.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.random.Random

class ImGoodsPageModel(application: Application) :BaseViewModelMVVM(application) {
    val showSearch by lazy {
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
    val targetName by lazy {
        MutableLiveData<String>()
    }
    var labelId=0
    var page=1
    val net by lazy { object :BaseRepository(){} }


    fun getImGoodsSearch(lableId:String?=null,keyword:String?=null) {
        val token = User.getToken()
        token ?: return

        SLog.info("${searchType.value}:请求数据")
        targetName.value ="u_007615414781"
        val queryParams = mapOf(
                User.getTokenPair(),
                this::targetName.run { name to get().value },
                this::searchType.run { name to get().value }
        )
        lableId?.apply { queryParams.plus("lableId" to this) }
        keyword?.apply { queryParams.plus("keyword" to this) }
//        queryParams.forEach{a,b ->SLog.info("$a,$b")}

        viewModelScope.launch(Dispatchers.Default) {
            withContext(Dispatchers.Main){
                try {
//                    val result=net.run { simpleGet(api.getImGoodsSearch(queryParams)) }
                    val result=net.run { simpleGet(api.getImGoodsSearch(queryParams)) }
                    when(result){
                        is Result.Success -> {
                            goodsList.value=result.datas.goodsList
//                        result.datas.storeLabelList?.run {storeLabelList.value=this}
                            stateLiveData.postSuccess()
                        }
                        is Result.DataError ->  {
                            errorMessage=result.datas.error
                            stateLiveData.postError()
                        }
                        is Result.Error ->  stateLiveData.postNoNet()
                        else -> stateLiveData.postIdle()
                    }
                }catch (e:Exception){
                    SLog.info("catch exception : ${e.toString()}")
                    stateLiveData.postError()
                }

            }
        }
    }
}
