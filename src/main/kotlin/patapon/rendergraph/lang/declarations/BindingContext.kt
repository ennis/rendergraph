package patapon.rendergraph.lang.declarations

import patapon.rendergraph.lang.psi.*
import patapon.rendergraph.lang.types.Type

interface BindingContext {
    val expressionTypes: MutableMap<RgExpression, Type>
    val moduleDeclarations: MutableMap<RgModule, ModuleDeclaration>
    val componentDeclarations: MutableMap<RgComponent, ComponentDeclaration>
    val variableDeclarations: MutableMap<RgVariable, VariableDeclaration>
    val functionDeclarations: MutableMap<RgFunction, FunctionDeclaration>
    val valueParameters: MutableMap<RgParameter, VariableDeclaration>
    val typeReferences: MutableMap<RgType, TypeDeclaration>
    val simpleReferences: MutableMap<RgSimpleReferenceExpression, Declaration>
    // cache for results of name lookup of component bases (component _: B, C, D)
    //val componentBases: MutableMap<RgPath, ComponentDeclaration>

    fun getDeclaration(element: RgDeclaration): Declaration? {
        return when (element)
        {
            is RgComponent -> componentDeclarations.get(element)
            is RgVariable -> variableDeclarations.get(element)
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
    override val variableDeclarations = mutableMapOf<RgVariable, VariableDeclaration>()
    override val functionDeclarations = mutableMapOf<RgFunction, FunctionDeclaration>()
    override val valueParameters = mutableMapOf<RgParameter, VariableDeclaration>()
    override val expressionTypes = mutableMapOf<RgExpression, Type>()
    override val moduleDeclarations = mutableMapOf<RgModule, ModuleDeclaration>()
    override val typeReferences = mutableMapOf<RgType, TypeDeclaration>()
    override val simpleReferences = mutableMapOf<RgSimpleReferenceExpression, Declaration>()
}
