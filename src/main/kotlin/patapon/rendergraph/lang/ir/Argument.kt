package patapon.rendergraph.lang.ir

import patapon.rendergraph.lang.psi.RgArgument

class Argument(val psi: RgArgument, override val containingDeclaration: Function): ValueDeclaration, LazyDeclaration
{
    val _type = Lazy<Type> {
        resolveArgumentType()
    }

    override val name = psi.identifier.text!!
    override val attributes: List<Attribute>
        get() = TODO("not implemented")
    override val type: Type
        get() = _type.value

    private fun resolveArgumentType(): Type {
        val resolver = TypeResolver(containingDeclaration.bodyResolutionScope.value)
        return resolver.resolveType(psi.type!!)
    }

    override fun forceFullResolve() {
        _type.doResolve()
    }
}
