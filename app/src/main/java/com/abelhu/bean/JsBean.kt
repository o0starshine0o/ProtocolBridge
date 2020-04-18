package com.abelhu.bean

/**
 * 模拟js数据的基类
 */
data class JsBean<T>(
    var status: String,
    var result: T
)