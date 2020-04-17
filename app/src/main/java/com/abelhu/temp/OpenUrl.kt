package com.abelhu.temp

import com.abelhu.bean.JsData
import com.abelhu.protocol.JsProtocol
import com.google.gson.Gson

/**
 * 根据JsProtocol.openUrl自动创建
 */
class OpenUrl(private val string: String) : JsProtocol.Stub {
    override fun run() {
        val data = Gson().fromJson(string, JsData::class.java)
        JsProtocol.openUrl(data)
    }
}