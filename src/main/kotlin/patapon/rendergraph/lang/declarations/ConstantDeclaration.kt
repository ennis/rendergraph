package patapon.rendergraph.lang.declarations

import patapon.rendergraph.lang.psi.RgConstant
import patapon.rendergraph.lang.types.Type
import patapon.rendergraph.lang.types.TypeResolver

interface ConstantDeclaration: Declaration {
    val type: Type
}

// the owning declaration is always resolved eagerly
class ConstantDeclarationImpl(
        override val owningDeclaration: DeclarationWithResolutionScope,
        override val name: String,
        val typeResolver: TypeResolver,
        constant: RgConstant) : ConstantDeclaration
{
    val type_ = Lazy {
        typeResolver.checkConstantDeclaration(
                constant,
                owningDeclaration.resolutionScope,
                owningDeclaration.resolutionScope)
    }

    override val type: Type
        get() = type_.value
}
