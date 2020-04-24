package com.abelhu.protocol_bridge_processor

import com.abelhu.protocalbridge.Protocol
import com.abelhu.utils.takeAll
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.RoundEnvironment
import javax.annotation.processing.SupportedOptions
import javax.annotation.processing.SupportedSourceVersion
import javax.lang.model.SourceVersion
import javax.lang.model.element.TypeElement

@SupportedSourceVersion(SourceVersion.RELEASE_8) // support java 8
@SupportedOptions(ProtocolProcessor.KAPT_KOTLIN_GENERATED_OPTION_NAME)
class ProtocolProcessor : AbstractProcessor() {
    companion object {
        const val SUFFIX = "Proxy"
        const val KAPT_KOTLIN_GENERATED_OPTION_NAME = "kapt.kotlin.generated"
    }

    /**
     * 设置支持的版本
     */
    override fun getSupportedSourceVersion(): SourceVersion = SourceVersion.latestSupported()

    /**
     * 添加需要支持的注解
     */
    override fun getSupportedAnnotationTypes(): MutableSet<String> = mutableSetOf(Protocol::class.java.canonicalName)

    /**
     * 处理注解的核心地方
     */
    override fun process(set: MutableSet<out TypeElement>?, environment: RoundEnvironment?): Boolean {
        // 获取被注解的元素
        val elements = environment?.getElementsAnnotatedWith(Protocol::class.java)
        // 只有被注解的接口才创建对应的类
        elements?.takeAll { element -> element.kind.isInterface }?.forEach { element ->
            // 找到所有需要注入的协议
            val protocols = elements.takeAll { protocol -> protocol.kind.isClass }
            // 根据需要注入的协议创建文件
            ProtocolProxyFileGenerate(processingEnv.filer, element, protocols).generateFile()
        }
        return true
    }

}