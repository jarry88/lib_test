package com.ftofs.twant.testcode

import io.reactivex.Observable
import okhttp3.Response

class TestRepository(private val datasource: RemoteDatasource) {
    fun test(): Observable<Response> =datasource.test()
}