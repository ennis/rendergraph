package patapon.rendergraph.lang.declarations

import patapon.rendergraph.lang.psi.RgVariable
import patapon.rendergraph.lang.resolve.Scope
import patapon.rendergraph.lang.types.Type
import patapon.rendergraph.lang.resolve.TypeResolver
import patapon.rendergraph.lang.utils.Lazy

interface VariableDeclaration : Declaration {
    override val owningDeclaration: DeclarationWithResolutionScope
    val type: Type
    val initializerResolutionScope: Scope
}

class VariableDeclarationImpl(
        override val owningDeclaration: DeclarationWithResolutionScope,
        override val name: String,
        override val type: Type,
        override val initializerResolutionScope: Scope) : VariableDeclaration
{
}

