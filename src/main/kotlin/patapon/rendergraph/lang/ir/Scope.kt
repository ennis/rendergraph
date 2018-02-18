package patapon.rendergraph.lang.ir


interface Scope
{
    fun getParentScope(): Scope?
    val owningDeclaration: Declaration?
    fun getAllDeclarations(): Collection<Declaration>
    fun findDeclarations(name: String): List<Declaration>
}

interface ScopeBuilder
{
    val scope: Scope
    fun addDeclaration(name: String, decl: Declaration): Boolean
    fun addDeclaration(decl: Declaration): Boolean {
        return addDeclaration(decl.name, decl)
    }
}

class ScopeImpl(
        val enclosingScope: LazyValue<Scope?>,
        override val owningDeclaration: Declaration?,
        initializer: ScopeBuilder.() -> Unit): Scope
{
    var symbolTable = mutableMapOf<String,Declaration>()

    override fun getParentScope(): Scope? {
        return enclosingScope.value
    }

    override fun getAllDeclarations(): Collection<Declaration> {
        return symbolTable.values
    }

    override fun findDeclarations(name: String): List<Declaration> {
        // this scope does not support symbols with the same name
        return listOfNotNull(symbolTable.get(name))
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
