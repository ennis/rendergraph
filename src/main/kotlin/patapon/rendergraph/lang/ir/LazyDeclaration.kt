package patapon.rendergraph.lang.ir

interface LazyDeclaration: Declaration
{
    fun forceFullResolve()
}
