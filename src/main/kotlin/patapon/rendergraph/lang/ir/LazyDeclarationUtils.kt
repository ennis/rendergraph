package patapon.rendergraph.lang.ir

fun Collection<Declaration>.forceFullResolve()
{
    forEach { decl -> if (decl is LazyDeclaration) decl.forceFullResolve() }
}
