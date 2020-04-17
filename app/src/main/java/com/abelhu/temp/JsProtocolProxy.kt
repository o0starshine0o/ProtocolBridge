package com.abelhu.temp

import com.abelhu.protocol.JsProtocol

/**
 * 根据JsProtocol自动创建
 */
class JsProtocolProxy private constructor() {
    companion object {
        val Instance: JsProtocolProxy by lazy { JsProtocolProxy() }
    }

    private val map = HashMap<String, String>()

    init {
        map["js_close"] = "com.abelhu.temp.Close"
        map["js_open_url"] = "com.abelhu.temp.OpenUrl"
        map["js_share_data"] = "com.abelhu.temp.ShareData"
    }

    /**
     * 有多个参数的情况
     * 如果需要传多个参数，需要把对应的类型和数据一起传过来
     *
     * @param protocol 协议名称
     * @param data 数据，需要转变成对应数据类型的数据，如果没有，获取无参的构造函数
     * @param types 不需要转变的数据的数据类型
     * @param params 不需要转变的数据
     */
    fun getProtocol(protocol: String, data: String = "", types: Array<Class<*>> = emptyArray(), params: Array<*> = emptyArray<Any>()): JsProtocol.Stub? {
        return map[protocol]?.let { clazzName ->
            val clazz = Class.forName(clazzName)
            val constructor = if (data.isEmpty()) clazz.getConstructor(*types) else clazz.getConstructor(data::class.java, *types)
            val protocolInstance = if (data.isEmpty()) constructor.newInstance(*params) else constructor.newInstance(data, *params)
            protocolInstance as? JsProtocol.Stub
        }
    }
}