package patapon.rendergraph.lang.declarations

fun Collection<Declaration>.forceFullResolve()
{
    forEach { decl -> decl.forceFullResolve() }
}
