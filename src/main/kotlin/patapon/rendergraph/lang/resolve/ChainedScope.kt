package patapon.rendergraph.lang.resolve

import patapon.rendergraph.lang.declarations.Declaration

class ChainedScope(vararg val scopes: Scope): Scope {
    override fun findDeclarations(name: String): Collection<Declaration> {
        scopes.forEach {
            val d = it.findDeclarations(name)
            if (!d.isEmpty()) {
                return d
            }
        }
        return emptyList()
    }

    override fun getAllDeclarations() = scopes.flatMap { it.getAllDeclarations() }
}