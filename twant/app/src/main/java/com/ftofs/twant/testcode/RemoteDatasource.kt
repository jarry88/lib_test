package com.ftofs.twant.testcode

import io.reactivex.Observable
import okhttp3.Response
import retrofit2.http.Body
import retrofit2.http.POST
import java.util.*

interface RemoteDatasource {
    @POST("test")
    fun test(@Body map: Map<String, String>): Observable<Response>
}