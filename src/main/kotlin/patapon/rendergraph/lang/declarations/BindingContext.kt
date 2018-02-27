package patapon.rendergraph.lang.declarations

import patapon.rendergraph.lang.psi.*
import patapon.rendergraph.lang.types.Type

interface BindingContext {
    val expressionTypes: MutableMap<RgExpression, Type>
    val moduleDeclarations: MutableMap<RgModule, ModuleDeclaration>
    val componentDeclarations: MutableMap<RgComponent, ComponentDeclaration>
    val constantDeclarations: MutableMap<RgVariable, ConstantDeclaration>
    val functionDeclarations: MutableMap<RgFunction, FunctionDeclaration>
    val valueParameters: MutableMap<RgParameter, ValueDeclaration>
    // cache for results of name lookup of component bases (component _: B, C, D)
    //val componentBases: MutableMap<RgPath, ComponentDeclaration>

    fun getDeclaration(element: RgDeclaration): Declaration? {
        return when (element)
        {
            is RgComponent -> componentDeclarations.get(element)
            is RgVariable -> constantDeclarations.get(element)
            is RgFunction -> functionDeclarations.get(element)
            else -> null
        }
    }

    fun getDeclarationWithResolutionScope(element: RgDeclaration): DeclarationWithResolutionScope? {
        return getDeclaration(element) as? DeclarationWithResolutionScope
    }

    fun getExpressionType(expression: RgExpression): Type? {
        return expressionTypes.get(expression)
    }
}

class BindingContextImpl: BindingContext
{
    override val componentDeclarations = mutableMapOf<RgComponent,ComponentDeclaration>()
    override val constantDeclarations = mutableMapOf<RgVariable, ConstantDeclaration>()
    override val functionDeclarations = mutableMapOf<RgFunction, FunctionDeclaration>()
    override val valueParameters = mutableMapOf<RgParameter, ValueDeclaration>()
    override val expressionTypes = mutableMapOf<RgExpression, Type>()
    override val moduleDeclarations = mutableMapOf<RgModule, ModuleDeclaration>()
}
