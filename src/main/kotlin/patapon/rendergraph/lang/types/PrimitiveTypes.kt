package patapon.rendergraph.lang.types

import patapon.rendergraph.lang.declarations.TypeDeclaration

const val FLOAT_TYPE_NAME: String = "float"
const val DOUBLE_TYPE_NAME: String = "double"
const val INT_TYPE_NAME: String = "int"
const val UNIT_TYPE_NAME: String = "void"
const val BOOLEAN_TYPE_NAME: String = "bool"
const val NOTHING_TYPE_NAME: String = "Nothing"

// TODO: simplify this, create the objects directly within PrimitiveScope
// no need for separate Type classes
abstract class PrimitiveType: NamedType()
{
}

object FloatType : PrimitiveType()
{
    override val name = FLOAT_TYPE_NAME
}

object DoubleType : PrimitiveType()
{
    override val name = DOUBLE_TYPE_NAME
}

object IntegerType :  PrimitiveType()
{
    override val name = INT_TYPE_NAME
}

object UnitType : PrimitiveType()
{
    override val name: String = UNIT_TYPE_NAME
}

object UnresolvedType: PrimitiveType()
{
    override val name = "<unresolved>"
}

object BooleanType: PrimitiveType()
{
    override val name: String = BOOLEAN_TYPE_NAME
}

// this type cannot be spelled: it has no declaration
object NothingType: PrimitiveType()
{
    override val name: String = NOTHING_TYPE_NAME
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
