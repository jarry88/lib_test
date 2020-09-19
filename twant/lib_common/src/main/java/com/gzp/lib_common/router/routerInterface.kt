package com.gzp.lib_common.router

import java.util.*

/**
 * 加载路由
 *
 * @author: Wuruiqiang <a href="mailto:263454190@qq.com">Contact me.</a>
 * @version: v1.0
 * @since: 18/1/4 下午6:38
 */
interface IRouteLoader {
    fun loadInto(map: MutableMap<String, RouteMetadata>)
}

/**
 * 加载拦截器
 *
 * @author: Wuruiqiang <a href="mailto:263454190@qq.com">Contact me.</a>
 * @version: v1.0
 * @since: 18/1/5 上午9:12
 */
interface IInterceptorLoader {
    fun loadInto(map: TreeMap<Int, InterceptorMetaData>)
}

/**
 * 加载Provider
 *
 * @author: Wuruiqiang <a href="mailto:263454190@qq.com">Contact me.</a>
 * @version: v1.0
 * @since: 18/1/5 上午9:12
 */
interface IProviderLoader {
    fun loadInto(map: MutableMap<String, Class<*>>)
}