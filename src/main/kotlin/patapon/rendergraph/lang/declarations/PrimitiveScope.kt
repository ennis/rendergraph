package patapon.rendergraph.lang.declarations

import patapon.rendergraph.lang.resolve.Scope
import patapon.rendergraph.lang.types.*

// The scope of all builtin/primitive declarations: types, functions, etc.
object PrimitiveScope: Scope {
    class PrimitiveTypeDeclaration(override val type: Type, override val name: String): TypeDeclaration

    override val owningDeclaration = null
    override fun getParentScope() = null

    override fun getAllDeclarations(): Collection<Declaration> {
        return symbolTable.values
    }

    override fun findDeclarations(name: String): List<Declaration> {
        return listOfNotNull(symbolTable.get(name))
    }

    val symbolTable = {
        fun primitiveTypeDecl(type: NamedType) = type.name to PrimitiveTypeDeclaration(type, type.name)
        mapOf<String, Declaration>(
            primitiveTypeDecl(FloatType),
            primitiveTypeDecl(DoubleType),
            primitiveTypeDecl(IntegerType),
            primitiveTypeDecl(UnitType),
            primitiveTypeDecl(BooleanType),
            primitiveTypeDecl(Vec2Type),
            primitiveTypeDecl(Vec3Type),
            primitiveTypeDecl(Vec4Type)
        )
    }()
}
