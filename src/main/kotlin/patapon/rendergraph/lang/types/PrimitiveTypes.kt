package patapon.rendergraph.lang.types

import patapon.rendergraph.lang.declarations.TypeDeclaration

const val FLOAT_TYPE_NAME: String = "float"
const val DOUBLE_TYPE_NAME: String = "double"
const val INT_TYPE_NAME: String = "int"
const val UNIT_TYPE_NAME: String = "void"
const val BOOLEAN_TYPE_NAME: String = "bool"

// TODO: simplify this, create the objects directly within PrimitiveScope
// no need for separate Type classes
abstract class PrimitiveType(val byteSize: Int): NamedType()
{
}

object FloatType : PrimitiveType(4)
{
    override val name = FLOAT_TYPE_NAME
}

object DoubleType : PrimitiveType(8)
{
    override val name = DOUBLE_TYPE_NAME
}

object IntegerType :  PrimitiveType(4)
{
    override val name = INT_TYPE_NAME
}

object UnitType : PrimitiveType(0)
{
    override val name: String = UNIT_TYPE_NAME
}

object UnresolvedType: PrimitiveType(0)
{
    override val name = "<unresolved>"
}

object BooleanType: PrimitiveType(1)
{
    override val name: String = BOOLEAN_TYPE_NAME
}

abstract class PrimitiveTypeDeclaration(override val type: Type, override val name: String): TypeDeclaration
{
}

// Declarations
object FloatTypeDeclaration: PrimitiveTypeDeclaration(FloatType, FLOAT_TYPE_NAME)
object DoubleTypeDeclaration: PrimitiveTypeDeclaration(DoubleType, DOUBLE_TYPE_NAME)
object IntegerTypeDeclaration: PrimitiveTypeDeclaration(IntegerType, INT_TYPE_NAME)
object UnitTypeDeclaration: PrimitiveTypeDeclaration(UnitType, UNIT_TYPE_NAME)
object BooleanTypeDeclaration: PrimitiveTypeDeclaration(BooleanType, BOOLEAN_TYPE_NAME)
