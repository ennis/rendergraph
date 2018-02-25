package patapon.rendergraph.lang.declarations

interface LazyDeclaration: Declaration
{
    fun forceFullResolve()
}
