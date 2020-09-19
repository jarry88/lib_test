package com.gzp.lib_common.config

object Config {
    var DEVELOPER_MODE: Boolean=true
    var OSS_BASE_URL = if (DEVELOPER_MODE) "https://ftofs-editor.oss-cn-shenzhen.aliyuncs.com" else "https://img.twant.com"
}