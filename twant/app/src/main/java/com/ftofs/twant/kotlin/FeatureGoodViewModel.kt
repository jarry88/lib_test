package com.ftofs.twant.kotlin

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.ftofs.twant.entity.SellerGoodsItem
import com.ftofs.twant.kotlin.base.BaseViewModel
import com.ftofs.twant.kotlin.vo.SellerPageVO
import com.ftofs.twant.util.User
import com.wzq.mvvmsmart.net.base.BaseResponse
import com.wzq.mvvmsmart.net.net_utils.RxUtil
import com.wzq.mvvmsmart.net.observer.DefaultObserver
import com.wzq.mvvmsmart.utils.KLog
import com.wzq.mvvmsmart.utils.ToastUtils
import io.reactivex.disposables.Disposable

class FeatureGoodViewModel(application: Application) : BaseViewModel(application) {
    private val viewModel: FeatureGoodModel = FeatureGoodModel()
    var pageNum = 1

    //    var liveData: MutableLiveData<MutableList<ItemsEntity?>> = MutableLiveData()
    val liveData: MutableLiveData<ArrayList<SellerGoodsItem>> by lazy {
        MutableLiveData<ArrayList<SellerGoodsItem>>()
    }
    /**
     * 网络请求方法，在ViewModel中调用Model层，通过Okhttp+Retrofit+RxJava发起请求
     * 改一下 去掉Rxjava
     */
    fun doGetFeaturesGoodsList() {
        //可以调用addSubscribe()添加Disposable，请求与View周期同步
        val observable = viewModel.doSellerGoodsList(params = mapOf("token" to User.getToken() , "page" to pageNum ,"goodsState" to 1))
        observable.compose(RxUtil.observableToMain()) //线程调度,compose操作符是直接对当前Observable进行操作（可简单理解为不停地.方法名（）.方法名（）链式操作当前Observable）
                .compose(RxUtil.exceptionTransformer()) // 网络错误的异常转换, 这里可以换成自己的ExceptionHandle
                .doOnSubscribe(this@FeatureGoodViewModel) //  请求与ViewModel周期同步
                .doOnSubscribe {
                    d ->
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
                            if (goodsList != null) {
                                if (goodsList.size > 0) {
                                    liveData.postValue(goodsList)
                                } else {
                                    //    showShortToast("没有更多数据了")
                                    KLog.e("请求到数据students.size" + goodsList.size)
                                }
                            } else {
                                KLog.e("数据返回null")
                                stateLiveData.postError()
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

    /**
     * 删除条目
     */
    fun deleteItem(newsData: SellerGoodsItem?) {
        //点击确定，在 observableList 绑定中删除，界面立即刷新

        KLog.e("调用了删除")
        KLog.e("size" + liveData.value?.size)
        val newsDataList = liveData.value
        newsDataList?.remove(newsData)
        KLog.e("size" + liveData.value?.size)
    }
}