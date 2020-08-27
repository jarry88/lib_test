package com.ftofs.twant.kotlin.testnet

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ftofs.twant.entity.SellerGoodsItem
import com.ftofs.twant.kotlin.LinkageModel
import com.ftofs.twant.kotlin.base.BaseViewModel
import com.ftofs.twant.kotlin.bean.NewsData
import com.ftofs.twant.kotlin.net.MRequest
import com.ftofs.twant.kotlin.vo.SellerPageVO
import com.ftofs.twant.log.SLog
import com.ftofs.twant.util.User
import com.wzq.mvvmsmart.utils.KLog
import com.wzq.mvvmsmart.net.base.BaseResponse
import com.wzq.mvvmsmart.net.net_utils.GsonUtil
import com.wzq.mvvmsmart.net.observer.DefaultObserver
import com.wzq.mvvmsmart.net.net_utils.RxUtil
import com.wzq.mvvmsmart.utils.ToastUtils
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

/**
 * 王志强
 * Create Date：2019/01/25
 * Description：业务类
 */
class TestNet2ViewModel(application: Application) : BaseViewModel(application) {
    //给RecyclerView添加ObservableList

    val viewModel:LinkageModel= LinkageModel()
    var pageNum:Int=1
    val liveData: MutableLiveData<ArrayList<SellerGoodsItem>> by lazy {
        MutableLiveData<ArrayList<SellerGoodsItem>>()
    }
    /**
    线程调度,compose操作符是直接对当前Observable进行操作（可简单理解为不停地.方法名（）.方法名（）链式操作当前Observable）
    网络错误的异常转换, 这里可以换成自己的ExceptionHandle
    请求与ViewModel周期同步
    获取个人信息
     */
    fun doPostServerNews() {
//        val param: Map<String, String> = HashMap()
        // 这里的参数是随便模拟的
        val param = mapOf("key" to 24,"name" to "zhangsan","age" to 25)
        MRequest.getInstance()?:ToastUtils.showShort("為空")
        val observable = MRequest.getInstance().doPostServerNews(GsonUtil.bean2String(param))
        observable.compose(RxUtil.observableToMain()) //线程调度,compose操作符是直接对当前Observable进行操作（可简单理解为不停地.方法名（）.方法名（）链式操作当前Observable）
                .compose(RxUtil.exceptionTransformer()) // 网络错误的异常转换, 这里可以换成自己的ExceptionHandle
                .doOnSubscribe(this@TestNet2ViewModel) //  请求与ViewModel周期同步
                .subscribe(object : DefaultObserver<ArrayList<NewsData>>() {
                    override fun onSubscribe(d: Disposable) {
                        stateLiveData.postLoading()
                    }

                    override fun onNext(baseResponse: BaseResponse<ArrayList<NewsData>>) {
                        super.onNext(baseResponse)
                        KLog.e("进入onNext")
                        // 请求成功
                        if (baseResponse.status == 1) {  // 接口返回code=1 代表成功
                            val news = baseResponse.datas
                            if (news != null) {
                                if (news.isNotEmpty()) {
//                                    liveData.postValue(SellerGoodsItem)
                                } else {
                                    //                                    showShortToast("没有更多数据了")
                                    KLog.e("请求到数据students.size" + news.size)
                                }
                            } else {
                                KLog.e("数据返回null")
                                stateLiveData.postError()
                            }
                        }
                    }

                    override fun onError(throwable: Throwable) {
                        KLog.e("进入onError" + throwable.message)
                        //关闭对话框
                        stateLiveData.postError()
                        /* if (throwable is ResponseThrowable) {
                         showShortToast(throwable.message)
                         }*/
                    }

                    override fun onComplete() {
                        KLog.e("进入onComplete")
                        //关闭对话框
                    }

                })

    }
    fun getTestData() {
        launch({},{},stateLiveData)
    }
    fun doScope(){
        viewModelScope.launch (Dispatchers.Default){
            withContext(Dispatchers.Main){
//                val result = viewModel.getShoppingGoodsList(zoneId)
                val param = mapOf("key" to 24,"name" to "zhangsan","age" to 25)

//                val result = viewModel.getShoppingGoodsList1(20)
                val result = MRequest.getInstance().service.doSellerFeaturesGoodsList(param)
                result.let { it.datas.let { SLog.info(it.toString())}}
                result.let { SLog.info("%d",it.code)}
            }
        }
    }
    fun doGetServerNews() {
        //可以调用addSubscribe()添加Disposable，请求与View周期同步
        val observable = doSellerGoodsList(params = mapOf("token" to User.getToken() , "page" to pageNum))
        observable.compose(RxUtil.observableToMain()) //线程调度,compose操作符是直接对当前Observable进行操作（可简单理解为不停地.方法名（）.方法名（）链式操作当前Observable）
                .compose(RxUtil.exceptionTransformer()) // 网络错误的异常转换, 这里可以换成自己的ExceptionHandle
                .doOnSubscribe(this@TestNet2ViewModel) //  请求与ViewModel周期同步
                .doOnSubscribe {
                    stateLiveData.postLoading()
                }
                .doFinally { stateLiveData.postIdle() }
                .subscribe(object : DefaultObserver<SellerPageVO<SellerGoodsItem>>() {
                    override fun onSubscribe(d: Disposable) {
                        super.onSubscribe(d)
                    }

                    override fun onNext(baseResponse: BaseResponse<SellerPageVO<SellerGoodsItem>>) {
                        super.onNext(baseResponse)
                        // 请求成功
                        if (baseResponse.code == 200) {  // 接口返回code=200 代表成功
                            val goodsList = baseResponse.datas.goodsList

                            //自定义处理
                            if (goodsList.size > 0) {
                                liveData.postValue(goodsList)
                            } else {
                                //    showShortToast("没有更多数据了")
                                KLog.e("请求到数据students.size" + goodsList.size)
                            }
                        } else {
                            //code错误时也可以定义Observable回调到View层去处理
                            ToastUtils.showShort("提醒开发者:本页无数据...")
                            KLog.e("请求失败response.getCode():" + baseResponse.code)
                            //                            liveData.postValue(null)
                        }
                    }

                    override fun onError(throwable: Throwable) {
                        super.onError(throwable)
                        KLog.e("进入onError" + throwable.message)
                        //关闭对话框
                        stateLiveData.postError()
                        /* if (throwable is ResponseThrowable) {
                         showShortToast(throwable.message)
                         }*/
                    }

                    override fun onComplete() {
                        super.onComplete()
                        //关闭对话框
                    }

                })

    }
    fun doSellerGoodsList(params:@JvmSuppressWildcards Map<String ,Any>): Observable<BaseResponse<SellerPageVO<SellerGoodsItem>>> {

        return MRequest.getInstance().doSellerGoodsList(params)
    }
}
