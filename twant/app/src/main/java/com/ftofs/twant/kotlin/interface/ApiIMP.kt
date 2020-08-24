package com.ftofs.twant.kotlin.`interface`

open interface ApiIMP {
    suspend fun  getData(params:HashMap<String,*>): String

}