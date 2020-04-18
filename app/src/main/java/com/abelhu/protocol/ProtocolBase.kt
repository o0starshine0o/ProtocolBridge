package com.abelhu.protocol

import android.text.TextUtils
import com.abelhu.bean.JsBean
import com.google.gson.Gson
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * 实现json字串到Bean的转换
 */
abstract class ProtocolBase<T>(private val json: String? = null) {
    val data: T?
        get() {
            if (TextUtils.isEmpty(json)) return null
            val type = (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments
            val parameterizedType = JsParameterizedType(JsBean::class.java, *type)
            return Gson().fromJson<JsBean<T>>(json, parameterizedType)?.result
        }

    class JsParameterizedType(private val raw: Class<*>, private vararg var types: Type) : ParameterizedType {
        override fun getRawType() = raw
        override fun getOwnerType() = null
        override fun getActualTypeArguments() = types
    }
}