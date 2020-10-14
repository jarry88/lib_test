package com.ftofs.twant.go853

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.ftofs.lib_net.BaseRepository
import com.ftofs.lib_net.DemoApiService
import com.ftofs.lib_net.model.GoeftInfo
import com.ftofs.lib_net.model.PropertyVo
import com.ftofs.twant.util.Util
import com.gzp.lib_common.base.BaseViewModel
import com.gzp.lib_common.utils.SLog
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

const val RENT_SALE_TYPE=1
const val SELLING_SALE_TYPE=2
const val RENT_AND_SELLING_TYPE=3
class GoHouseViewModel(application: Application):BaseViewModel(application) {
    val isTypeLiveData by lazy { MutableLiveData<Int>() }
    val toastError by lazy { MutableLiveData<String>() }
    val propertyTypeList=listOf("全部","住宅",
    "商鋪",
    "車位",
    "工業",
    "地皮","寫字樓","別墅")

    val saleTypeList=listOf("全部","出租",
    "出售",
    "售/租")
    val saleTypeLiveData by lazy { MutableLiveData<Int>(SELLING_SALE_TYPE) }

    val cityTypeList=listOf("全部","澳門","路氹","路環")
    val cityTypeLiveData by lazy { MutableLiveData<String>() }

    val rentPriceRangeList=listOf(
            factoryPriceMap("全部",0.0,Double.MAX_VALUE),
            factoryPriceMap("低於5000/月",0.0,5000.0),
            factoryPriceMap("5000-8000/月",5000.0,8000.0),
            factoryPriceMap("8000-10000/月",8000.0,10000.0),
            factoryPriceMap("10000-12000/月",10000.0,12000.0),
            factoryPriceMap("12000-15000/月",12000.0,15000.0),
            factoryPriceMap("15000-18000/月",15000.0,18000.0),
            factoryPriceMap("18000-20000/月",18000.0,20000.0),
            factoryPriceMap("高於20000/月",20000.0,Double.MAX_VALUE),
    )
    val rentPriceBeginLiveData by lazy { MutableLiveData<Double>() }
    val rentPriceEndLiveData by lazy { MutableLiveData<Double>() }
    val sellingPriceBeginLiveData by lazy { MutableLiveData<Double>() }
    val sellingPriceEndLiveData by lazy { MutableLiveData<Double>() }

    val sellingPriceRangeList=listOf(
            factorySellingPriceMap("全部",0.0,Double.MAX_VALUE),
            factorySellingPriceMap("低於200 萬",0.0,200.0),
            factorySellingPriceMap("200-400 萬",200.0,400.0),
            factorySellingPriceMap("400-600 萬",400.0,600.0),
            factorySellingPriceMap("600-800 萬",600.0,800.0),
            factorySellingPriceMap("800-1000 萬",800.0,1000.0),
            factorySellingPriceMap("1000-1200 萬",1000.0,1200.0),
            factorySellingPriceMap("1200-1400 萬",1200.0,1400.0),
            factorySellingPriceMap("高於1400 萬",1400.0,Double.MAX_VALUE),
    )

    private fun factorySellingPriceMap(desc: String?, sellingPriceBegin: Double?, sellingPriceEnd: Double?): Map<String, Any?> =
            mapOf("desc" to desc,"sellingPriceBegin" to sellingPriceBegin,"sellingPriceEnd" to sellingPriceEnd)
    private fun factoryPriceMap(desc: String?, rentalPriceBegin: Double?, rentalPriceEnd: Double?): Map<String, Any?> =
            mapOf("desc" to desc,"rentalPriceBegin" to rentalPriceBegin,"rentalPriceEnd" to rentalPriceEnd)
//    val cityTypeLiveData by lazy { MutableLiveData<String>() }

    var currUid: Int?=null
    private val repository by lazy { object :BaseRepository(){} }
    val propertyList by lazy { MutableLiveData<GoeftInfo>() }
    val userPropertyList by lazy { MutableLiveData<GoeftInfo>() }
    val retrofit = (Retrofit.Builder()).client(OkHttpClient.Builder().build()).addConverterFactory(GsonConverterFactory.create()).addCallAdapterFactory(RxJava2CallAdapterFactory.create()).baseUrl("http://192.168.5.19:8080/api/").build()
    val testApi=retrofit.create(DemoApiService::class.java)
    val currPropertyInfo by lazy { MutableLiveData<PropertyVo>() }

    var hasMore=true
    var currPage=0
    val finalApi =if(Util.inDev()) testApi else repository.api

    fun getPropertyDetail(pid: Int) {//獲取房產詳情
        launch(stateLiveData, {
//            val finalApi =if(Util.inDev()) testApi else api
            repository.run { simpleGet(finalApi.getPropertyInfo(pid)) }
        }, {
            currPropertyInfo.postValue(it)
        })
    }
    fun getPropertyList(
            page:Int=currPage+1,
            size:Int=20,
            search:String?=null,
            sort:String?=null,//editDate,排序字段名
            isType:Int?=allEqualsNull(isTypeLiveData.value) ,//房產類型
            saleType:Int?=allEqualsNull(saleTypeLiveData.value),//租售
            city:String?=cityTypeLiveData.value,//地理位置（區）：路氹、路環、新橋、關閘...等
            sellingPriceBegin:Double?=getSellingPriceBegin(),//售價區間：起；如果有 end，begin 的默認為0
            sellingPriceEnd:Double?=getSellingPriceEnd(),//	售價區間：止；如果有 begin，end 的默認為 maxint
            rentalPriceBegin:Double?=getRentPriceBegin(),//	售價區間：止；如果有 begin，end 的默認為 maxint
            rentalPriceEnd:Double?=getRentPriceEnd(),//	租價區間：止；如果有 begin，end 的默認為 maxint
            editDateBegin:String?=null,//	更新時間（"2006-01-02 15:04:05"）：起；如果有 end, begin默認為 30天前
            editDateEnd:String?=null,//	更新時間（"2006-01-02 15:04:05"）：止；如果有 begin, end默認為 nowt
    ) {
        launch(stateLiveData, {
            val params = mapOf<String,Any?>("page" to page, "size" to size)
                    .run {
                        if(search.isNullOrEmpty()) this
                        else this.plus("search" to search)
                    }.run {
                        if(sort.isNullOrEmpty()) this
                        else this.plus("sort" to sort)
                    }.run {

                        isType?.let {  this.plus("isType" to it) }?:this
                    }.run {
                        saleType?.let { this.plus("saleType" to it) }?:this
                    }.run {
                        if(city.isNullOrEmpty()) this
                        else this.plus("city" to city)
                    }.run {

                        sellingPriceBegin?.let { this.plus("sellingPriceBegin" to it) }?:this
                    }.run {

                        sellingPriceEnd?.let { this.plus("sellingPriceEnd" to it) }?:this
                    }.run {

                        rentalPriceBegin?.let { this.plus("rentalPriceBegin" to it) }?:this
                    }.run {

                        rentalPriceEnd?.let { this.plus("rentalPriceEnd" to it) }?:this
                    }.run {

                        editDateBegin?.let { this.plus("editDateBegin" to it) }?:this
                    }.run {
                        editDateEnd?.let { this.plus("editDateEnd" to it) }?:this
                    }
                repository.run {
                    simpleGet(finalApi.getPropertyInfoList(params.apply { SLog.info(this.toString()) }))
                }
        }, {
            hasMore = it.pageEntity.hasMore
            propertyList.postValue(it).apply {
                SLog.info("拿到數據${it.propertyList?.size}")
                it.propertyList?.let {l->
                    if (l.isNotEmpty()) {
                        currPage=page
                    }
                }
            }
        },error={
            it.error?.let {s->
                if(s.isNotEmpty()){
                    toastError.postValue(s)
                }
            }
        })
    }

    private fun getSellingPriceBegin(): Double? =
        saleTypeLiveData.value?.let {
            try {
                when(it){
                    SELLING_SALE_TYPE ->sellingPriceBeginLiveData.value
                    else ->null
                }
            }catch (e:Exception){
                return null
            }
        }
    private fun getSellingPriceEnd(): Double? =
        saleTypeLiveData.value?.let {
            try {
                when(it){
                    SELLING_SALE_TYPE ->sellingPriceEndLiveData.value
                    else ->null
                }
            }catch (e:Exception){
                return null
            }
        }
    private fun getRentPriceBegin(): Double? =
        saleTypeLiveData.value?.let {
            try {
                when(it){
                    RENT_SALE_TYPE ->rentPriceBeginLiveData.value
                    else ->null
                }
            }catch (e:Exception){
                return null
            }
        }
    private fun getRentPriceEnd(): Double? =
        saleTypeLiveData.value?.let {
            try {
                when(it){
                    RENT_SALE_TYPE ->rentPriceEndLiveData.value
                    else ->null
                }
            }catch (e:Exception){
                return null
            }
        }

    fun getUserPropertyList() {
    launch(stateLiveData,{repository.run {
        SLog.info("uid $currUid,page$currPage")
//        ,mapOf("page" to currPage+1,"size" to "20")
        simpleGet(finalApi.getGoeftUserUid(currUid?:0,mapOf("page" to currPage+1,"size" to "20"))) }},
            {userPropertyList.postValue(it)
                hasMore=it.pageEntity.hasMore
                it.propertyList?.let {list ->
                    if(list.isNotEmpty()) currPage++
                }
            })
    }

    fun savePropertyType(propertyType: String) {
        var index =0
        try {
            index=propertyTypeList.indexOf(propertyType)
        }catch (e:Exception){
            SLog.info(e.toString())
            index=-1
        }finally {
            if(index<0) index=0
        }
        isTypeLiveData.postValue(index)
    }
    fun saveSaleType(saleType: String?) {
        var index =0
        try {
            index=saleTypeList.indexOf(saleType)
        }catch (e:Exception){
            SLog.info(e.toString())
            index=-1
        }finally {
            if(index<0) index=0
        }
        saleTypeLiveData.postValue(index)
    }

    private fun allEqualsNull(index:Int?)=index?.let { if(index<=0) null else index }
    fun getPriceDescList(): List<String>? =saleTypeLiveData.value?.let {
        when(it){
            RENT_SALE_TYPE ->rentPriceRangeList.map { map ->  map["desc"] as String }
            SELLING_SALE_TYPE ->sellingPriceRangeList.map {  map ->  map["desc"] as String }
            RENT_AND_SELLING_TYPE ->null
            else -> null
        }
    }

    fun saveRentPriceRang(desc: String) {
        SLog.info(desc)
        rentPriceRangeList.first { (it["desc"] as String) == desc }?.let {
            rentPriceBeginLiveData.value=it.get("rentalPriceBegin") as Double?
            rentPriceEndLiveData.value=it.get("rentalPriceEnd") as Double?
        }?:{
            rentPriceBeginLiveData.value=null
            rentPriceEndLiveData.value=null
        }
    }
    fun saveSellingPriceRange(desc: String) {
        sellingPriceRangeList.first { (it["desc"] as String) == desc }?.let {
            sellingPriceBeginLiveData.value=it.get("sellingPriceBegin") as Double?
            sellingPriceEndLiveData.value=it.get("sellingPriceEnd") as Double?
        }?:{
            sellingPriceBeginLiveData.value=null
            sellingPriceEndLiveData.value=null
        }
    }

    fun saveCityString(city: String) {
        cityTypeLiveData.postValue(city)
    }

    fun clearPriceRange() {
        sellingPriceBeginLiveData.value=null
        sellingPriceEndLiveData.value=null
        rentPriceBeginLiveData.value=null
        rentPriceEndLiveData.value=null
    }
//    private fun factoryPriceMap(desc：String?,minPrice:Double,maxPrice:Double):Map<String,Any?>{
//
//    }

}
