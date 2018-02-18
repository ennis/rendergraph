package patapon.rendergraph.lang.ir

import com.intellij.psi.PsiElement
import patapon.rendergraph.lang.psi.RgPrimitiveType
import patapon.rendergraph.lang.psi.RgTokenType
import patapon.rendergraph.lang.psi.RgTokenTypes
import patapon.rendergraph.lang.psi.RgType

fun resolvePrimitiveTypes(path: Path): Type?
{
    if (path.pathSegments.size != 1) return null
    return when (path.pathSegments.first()) {
        // add your primitive type here
        "int" -> IntegerType
        "float" -> FloatType
        else -> null
    }
}

class TypeResolver(scope: Scope)
{
    fun resolveType(type: RgType): Type {
        val path = type.path
        val irPath = path.toIr()
        // check for primitive types
        val primitiveType = resolvePrimitiveTypes(irPath)
        if (primitiveType != null) return primitiveType
        // search for a type in scope
        TODO()
    }
}
