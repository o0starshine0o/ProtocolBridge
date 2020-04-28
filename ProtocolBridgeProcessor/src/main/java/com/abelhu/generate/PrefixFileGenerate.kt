package com.abelhu.generate

import com.squareup.kotlinpoet.*
import javax.annotation.processing.Filer

class PrefixFileGenerate(private val filer: Filer, private val prefix: String) {
    companion object {
        const val Protocol = "Protocol"
        const val Package = "com.abelhu.protocol"
        const val FunctionName = "dealProtocol"
    }

    private val name = ClassName(Package, "${prefix.capitalize()}$Protocol")

    fun generateFile() = FileSpec.builder(name.packageName, name.simpleName).addType(generateInterface()).build().apply { writeTo(filer) }

    private fun generateInterface() = TypeSpec.interfaceBuilder(name.simpleName).addFunction(generateFunction()).build()

    private fun generateFunction() = FunSpec.builder(FunctionName).addModifiers(KModifier.ABSTRACT).build()
}