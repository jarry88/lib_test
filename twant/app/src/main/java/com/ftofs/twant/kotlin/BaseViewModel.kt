package com.ftofs.twant.kotlin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.ftofs.twant.kotlin.vo.PageEntity
import com.ftofs.twant.kotlin.vo.SellerPageVO

/**
 * @dep
 */
@Deprecated("前期過度階段參考代碼，不建議使用，後面會移除")
open class BaseViewModel : ViewModel() {

    private val refreshTrigger = MutableLiveData<Boolean>()
    private val loading = MutableLiveData<Boolean>()
    protected val api = KotlinInterfaceApi.get()

    protected val page = MutableLiveData<Int>()
    private val refreshing = MutableLiveData<Boolean>()
    private val moreLoading = MutableLiveData<Boolean>()
    val hasMore = MutableLiveData<Boolean>()
    private val autoRefresh = MutableLiveData<Boolean>()//SmartRefreshLayout自动刷新标记

    fun loadMore() {
        page.value = (page.value ?: 0) + 1
        moreLoading.value = true
    }

    fun autoRefresh() {
        autoRefresh.value = true
    }

    open fun refresh() {//有些接口第一页可能为1，所以要重写
        page.value = 0
        refreshing.value = true
    }
    open fun loadData() {
        refreshTrigger.value = true
        loading.value = true
    }

    /**
     * 处理分页数据
     */
    fun <T> mapPage(source: LiveData<ApiResponse<PageEntity>>): LiveData<PageEntity> {
        return Transformations.map(source) {
            refreshing.value = false
            moreLoading.value = false
            hasMore.value = !(it?.datas?.over ?: false)

            it?.datas
        }
    } /**
     * 处理分页数据
     */
    fun <T> mapPageT(source: LiveData<ApiResponse<SellerPageVO<T>>>): LiveData<SellerPageVO<T>> {
        return Transformations.map(source) {
            refreshing.value = false
            moreLoading.value = false
            hasMore.value = !(it?.datas?.pageEntity?.hasMore ?: false)

            it?.datas
        }
    }

}