package patapon.rendergraph.lang.declarations

import patapon.rendergraph.lang.types.Type
import patapon.rendergraph.lang.types.TypeResolver
import patapon.rendergraph.lang.psi.RgParameter

class ParameterDeclaration(
        override val owningDeclaration: FunctionDeclaration,
        override val name: String,
        val index: Int,
        val typeResolver: TypeResolver,
        argument: RgParameter): ValueDeclaration
{
    val type_ = Lazy<Type> {
        typeResolver.checkFunctionArgumentType(argument, owningDeclaration.argumentResolutionScope)
    }

    override val type: Type
        get() = type_.value

}
