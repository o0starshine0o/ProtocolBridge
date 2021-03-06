package com.abelhu.generate

import com.abelhu.protocalbridge.Protocol
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import javax.annotation.processing.Filer
import javax.lang.model.element.Element

class ProtocolProxyFileGenerate(private val filer: Filer, file: FileSpec) {
    private val protocols: MutableList<Element> = mutableListOf()
    private val files: MutableList<Pair<String, ClassName>> = mutableListOf()
    private val protocolName = ClassName(file.packageName, file.name)
    private val className = ClassName(file.packageName, "${file.name}Proxy")

    fun generateFile() = FileSpec.builder(className.packageName, className.simpleName).addType(generateClass()).build().writeTo(filer)
    fun addProtocols(protocols: List<Element>) = apply { this.protocols.addAll(protocols) }
    fun addFiles(files: List<Pair<String, ClassName>>) = apply { this.files.addAll(files) }

    private fun generateClass(): TypeSpec {
        return TypeSpec.classBuilder(className.simpleName).primaryConstructor(FunSpec.constructorBuilder().addModifiers(KModifier.PRIVATE).build())
            .addType(generateInstance(className))
            .addProperty(PropertySpec.builder("map", generateHashMap(String::class.asTypeName())).initializer("HashMap()").build())
            .addInitializerBlock(generateInit())
            .addFunction(generateGetProtocolFunction())
            .build()
    }

    private fun generateInit() = CodeBlock.builder().apply {
        protocols.forEach { protocol -> addStatement("map[\"${protocol.getAnnotation(Protocol::class.java).protocol}\"] = \"${protocol}\"") }
        files.forEach { file -> addStatement("map[\"${file.first}\"] = \"${file.second}\"") }
    }.build()

    private fun generateGetProtocolFunction(): FunSpec {
        return FunSpec.builder("getProtocol")
            .addParameter("protocol", String::class)
            .addParameter(ParameterSpec.builder("data", String::class).defaultValue("\"\"").build())
            .addParameter(ParameterSpec.builder("params", generateParams()).defaultValue("emptyArray()").build())
            .addParameter(ParameterSpec.builder("types", generateParamsType()).defaultValue("params.map { it::class.java as Class<*> }.toTypedArray()").build())
            .addCode(generateFunction())
            .returns((protocolName as TypeName).copy(true))
            .addKdoc(generateComment())
            .build()
    }

    private fun generateInstance(className: ClassName): TypeSpec {
        return TypeSpec.companionObjectBuilder().apply {
            addProperty(PropertySpec.builder("Instance", className).apply {
                delegate(CodeBlock.builder().beginControlFlow("lazy").addStatement("${className.simpleName}()").endControlFlow().build())
            }.build())
        }.build()
    }

    private fun generateParamsType() = ARRAY.parameterizedBy(ClassName("java.lang", "Class").parameterizedBy(WildcardTypeName.producerOf(ANY)))

    private fun generateParams() = ARRAY.parameterizedBy(WildcardTypeName.producerOf(ANY))

    private fun generateFunction() = """
        return map[protocol]?.let { clazzName ->
            val clazz = Class.forName(clazzName)
            val constructor = if (data.isEmpty()) clazz.getConstructor(*types) else clazz.getConstructor(data::class.java, *types)
            val protocolInstance = if (data.isEmpty()) constructor.newInstance(*params) else constructor.newInstance(data, *params)
            protocolInstance as? JsProtocol
        }
        """.trimMargin()

    private fun generateComment() = """
                         有多个参数的情况
                         如果需要传多个参数，需要把对应的类型和数据一起传过来
                         
                         @param protocol 协议名称
                         @param data 数据，需要转变成对应数据类型的数据，如果没有，获取无参的构造函数
                         @param params 不需要转变的数据
                         @param types 协议中的参数类型（每一个type的class应该都是param的父类）
                """.trimIndent()

    private fun generateHashMap(type: TypeName) = generateHashMap(type, type)

    private fun generateHashMap(key: TypeName, value: TypeName) = ClassName("kotlin.collections", "HashMap").parameterizedBy(key, value)

}
