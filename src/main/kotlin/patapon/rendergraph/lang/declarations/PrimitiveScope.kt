package patapon.rendergraph.lang.declarations

import com.intellij.openapi.diagnostic.Logger
import com.intellij.psi.PsiElement
import patapon.rendergraph.lang.resolve.ChainedScope
import patapon.rendergraph.lang.resolve.Scope
import patapon.rendergraph.lang.types.*

// The scope of all builtin/primitive declarations: types, functions, etc.
object PrimitiveScope: Scope {
    var LOG = Logger.getInstance(ChainedScope::class.java)

    class PrimitiveTypeDeclaration(override val type: Type, override val name: String): TypeDeclaration {
        override val sourceElement: PsiElement?
            get() = null
    }

    override fun getAllDeclarations(): Collection<Declaration> {
        return symbolTable.values
    }

    override fun findDeclarations(name: String): List<Declaration> {
        val d = listOfNotNull(symbolTable.get(name))
        LOG.info("Lookup: $name into PrimitiveScope => ${d.size} results found")
        return d
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
            primitiveTypeDecl(Vec4Type),
            primitiveTypeDecl(Mat2x2Type),
            primitiveTypeDecl(Mat3x3Type),
            primitiveTypeDecl(Mat4x4Type)
        )
    }()
}
