package patapon.rendergraph.lang.declarations

fun Collection<Declaration>.forceFullResolve()
{
    forEach { decl -> if (decl is LazyDeclaration) decl.forceFullResolve() }
}
