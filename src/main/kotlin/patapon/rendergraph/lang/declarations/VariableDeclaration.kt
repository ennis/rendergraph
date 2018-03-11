package patapon.rendergraph.lang.declarations

import patapon.rendergraph.lang.psi.RgVariable
import patapon.rendergraph.lang.types.Type
import patapon.rendergraph.lang.resolve.TypeResolver
import patapon.rendergraph.lang.utils.Lazy

interface VariableDeclaration : Declaration {
    val type: Type
}

class VariableDeclarationImpl(
        override val owningDeclaration: DeclarationWithResolutionScope,
        override val name: String,
        val typeResolver: TypeResolver,
        variable: RgVariable) : VariableDeclaration
{
    val type_ = Lazy {
        typeResolver.checkVariableDeclaration(
                variable,
                owningDeclaration.resolutionScope,
                owningDeclaration.resolutionScope)
    }

    override val type: Type
        get() = type_.value

    override fun forceFullResolve() {
        type_.doResolve()
    }
}
