package com.ftofs.twant

import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@BeforeAll
fun init() {
    println("初始化數據")
}
@AfterAll
fun cleanUp() {
    println("清理數據")
}
@DisplayName("我的第一个测试用例")
class MyFirstTestCaseTest {

    @DisplayName("我的第一個測試")
    @Test
    fun testFirstTest(){println("第一個測試")}
}