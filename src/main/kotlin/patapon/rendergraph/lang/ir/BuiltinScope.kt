package patapon.rendergraph.lang.ir

// The scope of all builtin/primitive declarations: types, functions, etc.
object BuiltinScope: Scope {
    override val owningDeclaration = null
    override fun getParentScope() = null

    override fun getAllDeclarations(): Collection<Declaration> {
        return symbolTable.values
    }

    override fun findDeclarations(name: String): List<Declaration> {
        return listOfNotNull(symbolTable.get(name))
    }

    val symbolTable = mapOf<String,Declaration>(
            // XXX check that the names are different?
            IntegerTypeDeclaration.name to IntegerTypeDeclaration,
            FloatTypeDeclaration.name to FloatTypeDeclaration,
            DoubleTypeDeclaration.name to DoubleTypeDeclaration,
            UnitTypeDeclaration.name to UnitTypeDeclaration
    )
}
