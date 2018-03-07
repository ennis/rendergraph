package patapon.rendergraph.lang.resolve

import patapon.rendergraph.lang.declarations.Declaration

class SimpleScope(
        val enclosingScope: Scope?,
        initializer: Builder.() -> Unit) : Scope
{
    interface Builder {
        val scope: Scope
        fun addDeclaration(name: String, decl: Declaration): Boolean
        fun addDeclaration(decl: Declaration): Boolean {
            return addDeclaration(decl.name, decl)
        }
    }

    var symbolTable = mutableMapOf<String, Declaration>()

    fun getParentScope(): Scope? {
        return enclosingScope
    }

    override fun getAllDeclarations(): Collection<Declaration> {
        return symbolTable.values
    }

    override fun findDeclarations(name: String): Collection<Declaration> {
        // this scope does not support symbols with the same name
        val result = symbolTable.get(name)
        return if (result != null) {
            // found a result in this scope
            listOf(result)
        } else {
            // look into enclosing scopes
            val parent = getParentScope()
            if (parent != null) {
                parent.findDeclarations(name)
            } else {
                emptyList()
            }
        }
    }

    init {
        val builder = object : Builder {
            override val scope = this@SimpleScope
            override fun addDeclaration(name: String, decl: Declaration): Boolean {
                return symbolTable.putIfAbsent(name, decl) == null
            }
        }
        builder.initializer()
    }
}
