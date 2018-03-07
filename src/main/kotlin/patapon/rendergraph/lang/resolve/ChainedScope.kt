package patapon.rendergraph.lang.resolve

import com.intellij.openapi.diagnostic.Logger
import patapon.rendergraph.lang.declarations.Declaration

class ChainedScope(vararg val scopes: Scope): Scope {
    companion object {
        var LOG = Logger.getInstance(ChainedScope::class.java)
    }

    override fun findDeclarations(name: String): Collection<Declaration> {
        scopes.forEach {
            val d = it.findDeclarations(name)
            LOG.info("Lookup: $name into ${it} => ${d.size} results found")
            if (!d.isEmpty()) {
                return d
            }
        }
        return emptyList()
    }

    override fun getAllDeclarations() = scopes.flatMap { it.getAllDeclarations() }
}