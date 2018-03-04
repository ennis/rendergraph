package patapon.rendergraph.lang.declarations

import patapon.rendergraph.lang.types.Type
import patapon.rendergraph.lang.types.TypeResolver
import patapon.rendergraph.lang.psi.RgParameter
import patapon.rendergraph.lang.utils.Lazy

class ValueParameter(
        override val owningDeclaration: FunctionDeclaration,
        override val name: String,
        val index: Int,
        val typeResolver: TypeResolver,
        parameter: RgParameter): ValueDeclaration
{
    val type_ = Lazy {
        typeResolver.checkFunctionArgumentType(parameter, owningDeclaration.argumentResolutionScope)
    }

    override val type: Type
        get() = type_.value

}
