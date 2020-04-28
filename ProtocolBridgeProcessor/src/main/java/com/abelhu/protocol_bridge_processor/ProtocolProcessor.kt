package com.abelhu.protocol_bridge_processor

import com.abelhu.generate.ClassFileGenerate
import com.abelhu.generate.PrefixFileGenerate
import com.abelhu.generate.ProtocolProxyFileGenerate
import com.abelhu.protocalbridge.Protocol
import com.abelhu.utils.protocolName
import com.abelhu.utils.takeAll
import java.util.*
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.Filer
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.TypeElement

class ProtocolProcessor : AbstractProcessor() {
    private lateinit var processingEnvironment: ProcessingEnvironment
    private lateinit var filer: Filer

    @Synchronized
    override fun init(processingEnvironment: ProcessingEnvironment) {
        super.init(processingEnvironment)
        this.processingEnvironment = processingEnvironment
        filer = processingEnvironment.filer
    }

    /**
     * 指定使用的Java版本
     */
    override fun getSupportedSourceVersion(): SourceVersion = SourceVersion.latestSupported()

    /**
     * 添加需要支持的注解
     */
    override fun getSupportedAnnotationTypes(): MutableSet<String> = mutableSetOf(Protocol::class.java.canonicalName)

    /**
     * 处理注解的核心地方
     */
    override fun process(set: MutableSet<out TypeElement>, environment: RoundEnvironment): Boolean {
        // 获取被注解的所有元素
        val elements = environment.getElementsAnnotatedWith(Protocol::class.java)
        // 整理${prefix}的对应关系
        val protocolMap = HashMap<String, MutableList<Element>>()
        for (element in elements) {
            val prefix = element.getAnnotation(Protocol::class.java).prefix
            protocolMap.getOrPut(prefix) { mutableListOf() }.add(element)
        }
        // 对每一组对应的${prefix}关系处理
        for ((name, elementList) in protocolMap.entries) {
            // 对出现的所有${prefix}都生成对应的"${prefix}Protocol"接口
            val protocol = PrefixFileGenerate(filer, name).generateFile()
            // 对所有注解到方法上的元素，生成${Function}类
            val files = elementList.takeAll { element -> element is ExecutableElement }
                .map { Pair(it.protocolName(), ClassFileGenerate(filer, it as ExecutableElement, protocol).generateFile()) }
            // 利用生成的${Function}类和被注解标记的类生成"${prefix}ProtocolProxy"
            ProtocolProxyFileGenerate(filer, protocol).addProtocols(elementList.takeAll { element -> element.kind.isClass }).addFiles(files).generateFile()
        }
        return true
    }

}