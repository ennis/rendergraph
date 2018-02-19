package patapon.rendergraph.lang.ir

const val FLOAT_TYPE_NAME: String = "float"
const val DOUBLE_TYPE_NAME: String = "double"
const val INT_TYPE_NAME: String = "int"
const val UNIT_TYPE_NAME: String = "void"

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

abstract class PrimitiveTypeDeclaration(override val type: Type, override val name: String): TypeDeclaration
{
    override val attributes = emptyList<Attribute>()
}

// Declarations
object FloatTypeDeclaration: PrimitiveTypeDeclaration(FloatType, FLOAT_TYPE_NAME)
object DoubleTypeDeclaration: PrimitiveTypeDeclaration(DoubleType, DOUBLE_TYPE_NAME)
object IntegerTypeDeclaration: PrimitiveTypeDeclaration(IntegerType, INT_TYPE_NAME)
object UnitTypeDeclaration: PrimitiveTypeDeclaration(UnitType, UNIT_TYPE_NAME)
