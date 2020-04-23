package com.abelhu.protocol_bridge_processor

import com.abelhu.protocalbridge.Protocol
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import javax.annotation.processing.Filer
import javax.lang.model.element.Element

class ProtocolProxyFileGenerate(private val filer: Filer, private val element: Element, private val protocols: List<Element>) {
    private val protocolName = ClassName("${element.enclosingElement}", listOf("${element.simpleName}"))
    private val className = ClassName("${element.enclosingElement}", "${element.simpleName}${ProtocolProcessor.SUFFIX}")
    fun generateFile() {
        FileSpec.builder(className.packageName, className.simpleName).addType(generateClass(element, protocols)).build().writeTo(filer)
    }

    private fun generateClass(element: Element, protocols: List<Element>): TypeSpec {
        return TypeSpec.classBuilder(className.simpleName).primaryConstructor(FunSpec.constructorBuilder().addModifiers(KModifier.PRIVATE).build())
            .addType(generateInstance(className))
            .addProperty(PropertySpec.builder("map", generateHashMap(String::class.asTypeName())).initializer("HashMap()").build())
            .addInitializerBlock(generateInit(protocols))
            .addFunction(generateGetProtocolFunction())
            .build()
    }

    private fun generateInit(protocols: List<Element>): CodeBlock {
        return CodeBlock.builder().apply {
            protocols.forEach { protocol ->
                addStatement("map[\"${protocol.getAnnotation(Protocol::class.java).protocol}\"] = \"${protocol}\"")
            }
        }.build()
    }

    private fun generateGetProtocolFunction(): FunSpec {
        return FunSpec.builder("getProtocol")
            .addAnnotation(JvmOverloads::class.java)
            .addParameter("protocol", String::class)
            .addParameter(ParameterSpec.builder("data", String::class).defaultValue("\"\"").build())
            .addParameter(
                ParameterSpec.builder(
                    "types", ARRAY.parameterizedBy(
                        ClassName("java.lang", "Class")
                            .parameterizedBy(WildcardTypeName.producerOf(ClassName("kotlin", "Any")))
                    )
                ).defaultValue("emptyArray()").build()
            )
            .addParameter(
                ParameterSpec.builder("params", ARRAY.parameterizedBy(WildcardTypeName.producerOf(ClassName("kotlin", "Any"))))
                    .defaultValue("emptyArray()").build()
            )
            .addCode(
                """
        return map[protocol]?.let { clazzName ->
            val clazz = Class.forName(clazzName)
            val constructor = if (data.isEmpty()) clazz.getConstructor(*types) else clazz.getConstructor(data::class.java, *types)
            val protocolInstance = if (data.isEmpty()) constructor.newInstance(*params) else constructor.newInstance(data, *params)
            protocolInstance as? JsProtocol
        }
        """.trimMargin()
            ).returns((protocolName as TypeName).copy(true))
            .addKdoc(
                """
                         有多个参数的情况
                         如果需要传多个参数，需要把对应的类型和数据一起传过来
                         
                         @param protocol 协议名称
                         @param data 数据，需要转变成对应数据类型的数据，如果没有，获取无参的构造函数
                         @param types 不需要转变的数据的数据类型
                         @param params 不需要转变的数据
                """.trimIndent()
            )
            .build()
    }

    private fun generateInstance(className: ClassName): TypeSpec {
        return TypeSpec.companionObjectBuilder().apply {
            addProperty(PropertySpec.builder("Instance", className).apply {
                delegate(CodeBlock.builder().beginControlFlow("lazy").addStatement("${className.simpleName}()").endControlFlow().build())
            }.build())
        }.build()
    }

    private fun generateHashMap(type: TypeName) = generateHashMap(type, type)

    private fun generateHashMap(key: TypeName, value: TypeName) = ClassName("kotlin.collections", "HashMap").parameterizedBy(key, value)

}
