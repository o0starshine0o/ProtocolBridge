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
        map["js_open_url"] = "com.abelhu.temp.OpenUrl"
    }

    /**
     * 对应没有参数的情况，直接调用
     */
    fun getProtocol(key: String): JsProtocol.Stub? {
        return map[key]?.let {clazzName->
            val clazz = Class.forName(clazzName)
            val constructors = clazz.constructors
            val constructor = clazz.getConstructor()
            val protocol = constructor.newInstance()
            protocol as? JsProtocol.Stub
        }
    }

    /**
     * 只有一个string类型的参数，把string类型的参数转换成对应的数据类型
     */
    fun getProtocol(key: String, data:String): JsProtocol.Stub? {
        return map[key]?.let {clazzName->
            val clazz = Class.forName(clazzName)
            val constructors = clazz.constructors
            val constructor = clazz.getConstructor(String::class.javaPrimitiveType)
            val protocol = constructor.newInstance(data)
            protocol as? JsProtocol.Stub
        }
    }

    /**
     * 有多个参数的情况
     * 如果需要传多个参数，需要把对应的类型和数据一起传过来
     */
    fun getProtocol(key: String, types: Array<Class<*>>, params:Array<*>): JsProtocol.Stub? {
        return map[key]?.let {clazzName->
            val clazz = Class.forName(clazzName)
            val constructor = clazz.getConstructor(*types)
            val protocol = constructor.newInstance(*params)
            protocol as? JsProtocol.Stub
        }
    }
}