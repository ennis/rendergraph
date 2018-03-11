package patapon.rendergraph.lang.resolve

import patapon.rendergraph.lang.declarations.Declaration
import patapon.rendergraph.lang.psi.RgPath
import patapon.rendergraph.lang.psi.RgPathRoot
import patapon.rendergraph.lang.psi.RgQualifiedPath

fun<T: Declaration> Collection<T>.ensureUnique(): Declaration?
{
    if (size > 1) return null
    return firstOrNull()
}


fun resolvePath(path: RgPath, scope: Scope): Collection<Declaration> {
    return if (path is RgQualifiedPath) {
        TODO("resolve qualified paths")
    } else {
        val path = path as RgPathRoot
        scope.findDeclarations(path.identifier.text)
    }
}
