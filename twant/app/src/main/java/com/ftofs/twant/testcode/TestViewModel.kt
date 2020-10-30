package com.ftofs.twant.testcode

import com.ftofs.twant.TwantApplication
import com.gzp.lib_common.base.BaseViewModel

class TestViewModel(application: TwantApplication,private val repository: TestRepository) : BaseViewModel(application) {
    fun test()=repository.test()
}