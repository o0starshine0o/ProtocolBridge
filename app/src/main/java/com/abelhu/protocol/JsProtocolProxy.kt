//package com.abelhu.protocol
//
///**
// * 根据JsProtocol自动创建
// */
//class JsProtocolProxy private constructor() {
//    companion object {
//        val Instance by lazy { JsProtocolProxy() }
//    }
//
//    private val map:HashMap<String, String> = HashMap()
//
//    init {
//        map["js_close"] = "com.abelhu.protocol.Close"
//        map["js_open_url"] = "com.abelhu.protocol.OpenUrl"
//        map["js_share_data"] = "com.abelhu.protocol.ShareData"
//    }
//
//    /**
//     * 有多个参数的情况
//     * 如果需要传多个参数，需要把对应的类型和数据一起传过来
//     *
//     * @param protocol 协议名称
//     * @param data 数据，需要转变成对应数据类型的数据，如果没有，获取无参的构造函数
//     * @param params 不需要转变的数据
//     * @param types 协议中的参数类型（每一个type的class应该都是param的父类）
//     */
//    fun getProtocol(protocol: String, data: String = "", params: Array<out Any> = emptyArray(), types:Array<Class<Any>> = emptyArray()): JsProtocol? {
//        return map[protocol]?.let { clazzName ->
//            val clazz = Class.forName(clazzName)
//            val constructor = if (data.isEmpty()) clazz.getConstructor(*types) else clazz.getConstructor(data::class.java, *types)
//            val protocolInstance = if (data.isEmpty()) constructor.newInstance(*params) else constructor.newInstance(data, *params)
//            protocolInstance as? JsProtocol
//        }
//    }
//}