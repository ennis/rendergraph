package patapon.rendergraph.lang.resolve

import patapon.rendergraph.lang.declarations.Declaration

fun<T: Declaration> Collection<T>.ensureUnique(): Declaration?
{
    if (size > 1) return null
    return firstOrNull()
}

