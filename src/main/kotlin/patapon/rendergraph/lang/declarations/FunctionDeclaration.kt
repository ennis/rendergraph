package patapon.rendergraph.lang.declarations

import com.intellij.psi.PsiElement
import patapon.rendergraph.lang.psi.RgFunction
import patapon.rendergraph.lang.resolve.DeclarationResolver
import patapon.rendergraph.lang.resolve.TypeResolver
import patapon.rendergraph.lang.resolve.Scope
import patapon.rendergraph.lang.types.FunctionType
import patapon.rendergraph.lang.types.Type
import patapon.rendergraph.lang.utils.Lazy


interface FunctionDeclaration : DeclarationWithResolutionScope {
    override val owningDeclaration: DeclarationWithResolutionScope
    val parameterResolutionScope: Scope
    val isOperator: Boolean
    val signature: FunctionType
    val valueParameters: List<ValueParameter>
}

class FunctionDeclarationImpl(
        override val owningDeclaration: DeclarationWithResolutionScope,
        override val sourceElement: RgFunction,
        override val name: String,
        override val isOperator: Boolean) : FunctionDeclaration
{
    // lateinit because of a cyclic dependency between FunctionDeclarations and ValueParameters
    lateinit var bodyResolutionScope: Scope
    lateinit override var valueParameters: List<ValueParameter>
    lateinit override var signature: FunctionType

    override val parameterResolutionScope = owningDeclaration.resolutionScope

    override val resolutionScope: Scope
        get() = bodyResolutionScope
}
