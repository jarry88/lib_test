package com.ftofs.twant.kotlin

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations

class SearchVM : BaseViewModel() {
    val keyword = MutableLiveData<String>()

//    //类型为LiveData<ApiResponse<PageVO<ArticleVO>>>
//    private val _articlePage = Transformations.switchMap(page) {
//        api.searchArticlePage(it, keyword.value ?: "")
//    }
//    //类型LiveData<PageVO<ArticleVO>>
//    val articlePage = mapPage(_articlePage)

    fun search() {//搜索数据
        autoRefresh()
    }

}