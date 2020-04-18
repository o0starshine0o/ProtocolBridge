package com.abelhu.protocol

import android.util.Log
import com.abelhu.bean.JsData
import com.abelhu.protocalbridge.Protocol
import com.google.gson.Gson

/**
 * 应用Protocol注解，参数为协议名称，之后（demo为MainActivity）根据协议名称创建实例
 * 一定要实现JsProtocol接口
 * 可以不继承ProtocolBase，ProtocolBase类只是为了方便json转bean
 */
@Protocol("js_open_url")
class OpenUrl(string: String) :ProtocolBase<JsData>(string), JsProtocol {
    override fun dealProtocol() {
        Log.i("OpenUrl", "openUrl:${data?.js}")
    }
}