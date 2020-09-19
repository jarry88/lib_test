package com.gzp.lib_common.router

import java.lang.annotation.ElementType
import java.lang.annotation.RetentionPolicy

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class Route (
    /**
     * Path of route
     */
    val path: String,
    /**
     * PathPrefix of route
     */
    val PathPrefix : String = "",
    /**
     * PathPattern of route
     */
    val pathPattern: String = "",
    /**
     * Name of route
     */
    val name: String = "undefined",
    /**
     * Priority of route
     */
    val priority: Int = -1
)
/**
 * User: WuRuiqiang(263454190@qq.com)
 * Date: 18/1/2
 * Time: 上午10:53
 * Version: v1.0
 * Description：用于拦截路由的拦截器
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class Interceptor(
        /**
         * Priority of interceptor
         */
        val priority: Int = -1,
        /**
         * Name of interceptor
         */
        val name: String = "DefaultInterceptor")

/**
 * User: WuRuiqiang(263454190@qq.com)
 * Date: 18/1/2
 * Time: 上午10:53
 * Version: v1.0
 * Description：属性注入
 */
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.SOURCE)
annotation class Inject(
        /**
         * Name of property
         */
        val name: String = "",
        /**
         * If true, app will be throws NPE when value is null
         */
        val isRequired: Boolean = false,
        /**
         * Description of the field
         */
        val desc: String = "No desc.")

/**
 * User: WuRuiqiang(263454190@qq.com)
 * Date: 18/1/2
 * Time: 上午10:53
 * Version: v1.0
 * Description：Provider
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class Provider(/**
                           * Path of Provider
                           */
                          val value: String)
/**
 * User: WuRuiqiang(263454190@qq.com)
 * Date: 18/1/4
 * Time: 上午10:46
 * Version: v1.0
 * Description：Route元数据，用于存储被[com.github.richardwrq.krouter.annotation.Route]注解的类的信息
 */
data class RouteMetadata(
        /**
         * Type of Route
         */
//        val routeType: RouteType = RouteType.UNKNOWN,
        /**
         * Priority of route
         */
        val priority: Int = -1,
        /**
         * Name of route
         */
        val name: String = "undefine",
        /**
         * Path of route
         */
        val path: String = "",
        /**
         * PathPrefix of route
         */
        val pathPrefix: String = "",
        /**
         * PathPattern of route
         */
        val pathPattern: String = "",
        /**
         * Class of route
         */
        val clazz: Class<*> = Any::class.java)
/**
 * User: WuRuiqiang(263454190@qq.com)
 * Date: 18/1/8
 * Time: 下午10:46
 * Version: v1.0
 * Description：Interceptor元数据，用于存储被[com.github.richardwrq.krouter.annotation.Interceptor]注解的类的信息
 */
data class InterceptorMetaData(
        /**
         * Priority of Interceptor
         */
        val priority: Int = -1,
        /**
         * Name of Interceptor
         */
        val name: String = "undefine",
        /**
         * Class desc of Interceptor
         */
        val clazz: Class<*> = Any::class.java)

/**
 * User: WuRuiqiang(263454190@qq.com)
 * Date: 18/3/14
 * Time: 上午1:28
 * Version: v1.0
 * Description：Injector元数据，用于存储被[com.github.richardwrq.krouter.annotation.Inject]注解的类的信息
 */
data class InjectorMetaData(
        /**
         * if true, throw NPE when the filed is null
         */
        val isRequired: Boolean = false,
        /**
         * key
         */
        val key: String = "",
        /**
         * field name
         */
        val fieldName: String = "")
