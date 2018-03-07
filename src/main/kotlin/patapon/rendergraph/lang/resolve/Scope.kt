package patapon.rendergraph.lang.resolve

import patapon.rendergraph.lang.declarations.Declaration

interface Scope {
    fun getAllDeclarations(): Collection<Declaration>
    fun findDeclarations(name: String): Collection<Declaration>

    //fun getFunctionDeclarations(name: String): Collection<FunctionDeclaration>
    //fun getVariableDeclarations(name: String): Collection<VariableDeclaration>
    //fun getComponentDeclarations(name: String): Collection<ComponentDeclaration>
}

interface ScopeBuilder {
    val scope: Scope
    fun addDeclaration(name: String, decl: Declaration): Boolean
    fun addDeclaration(decl: Declaration): Boolean {
        return addDeclaration(decl.name, decl)
    }
}

class ScopeImpl(
        val enclosingScope: Scope?,
        initializer: ScopeBuilder.() -> Unit) : Scope {
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
        val builder = object : ScopeBuilder {
            override val scope = this@ScopeImpl
            override fun addDeclaration(name: String, decl: Declaration): Boolean {
                // TODO handle redeclarations in a generic way
                return symbolTable.putIfAbsent(name, decl) == null
            }
        }
        builder.initializer()
    }
}
