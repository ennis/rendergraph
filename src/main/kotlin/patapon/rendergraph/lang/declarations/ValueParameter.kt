package patapon.rendergraph.lang.declarations

import com.intellij.psi.PsiElement
import patapon.rendergraph.lang.types.Type
import patapon.rendergraph.lang.resolve.TypeResolver
import patapon.rendergraph.lang.psi.RgParameter
import patapon.rendergraph.lang.resolve.Scope
import patapon.rendergraph.lang.utils.Lazy

class ValueParameter(
        override val owningDeclaration: FunctionDeclaration,
        override val sourceElement: RgParameter,
        override val name: String,
        override val type: Type,
        val index: Int): VariableDeclaration
{
    override val initializerResolutionScope: Scope
        get() = owningDeclaration.parameterResolutionScope
}
