package patapon.rendergraph.lang.declarations

import patapon.rendergraph.lang.psi.RgVariable
import patapon.rendergraph.lang.types.Type
import patapon.rendergraph.lang.types.TypeResolver
import patapon.rendergraph.lang.utils.Lazy

interface VariableDeclaration : Declaration {
    val type: Type
}

// the owning declaration is always resolved eagerly
class VariableDeclarationImpl(
        override val owningDeclaration: DeclarationWithResolutionScope,
        override val name: String,
        val typeResolver: TypeResolver,
        variable: RgVariable) : VariableDeclaration
{
    val type_ = Lazy {
        typeResolver.checkConstantDeclaration(
                variable,
                owningDeclaration.resolutionScope,
                owningDeclaration.resolutionScope)
    }

    override val type: Type
        get() = type_.value
}
