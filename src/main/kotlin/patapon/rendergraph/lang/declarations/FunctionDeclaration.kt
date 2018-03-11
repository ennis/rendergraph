package patapon.rendergraph.lang.declarations

import patapon.rendergraph.lang.psi.RgFunction
import patapon.rendergraph.lang.resolve.DeclarationResolver
import patapon.rendergraph.lang.resolve.TypeResolver
import patapon.rendergraph.lang.resolve.Scope
import patapon.rendergraph.lang.types.Type
import patapon.rendergraph.lang.utils.Lazy


interface FunctionDeclaration : DeclarationWithResolutionScope {
    override val owningDeclaration: DeclarationWithResolutionScope
    val parameterResolutionScope: Scope
    val returnType: Type
}

class FunctionDeclarationImpl(
        override val owningDeclaration: DeclarationWithResolutionScope,
        override val name: String) : FunctionDeclaration
{
    // lateinit because of a cyclic dependency between FunctionDeclarations and ValueParameters
    lateinit var bodyResolutionScope: Scope
    lateinit override var returnType: Type

    override val parameterResolutionScope = owningDeclaration.resolutionScope

    override val resolutionScope: Scope
        get() = bodyResolutionScope
}
