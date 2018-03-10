package patapon.rendergraph.lang.types

abstract class PrimitiveType(override val name: String): NamedType()

object FloatType : PrimitiveType("float")
object DoubleType : PrimitiveType("double")
object IntegerType :  PrimitiveType("int")
object UnitType : PrimitiveType("void")
object UnresolvedType: PrimitiveType("<unresolved>")
object BooleanType: PrimitiveType("bool")
object NothingType: PrimitiveType("Nothing")

abstract class PrimitiveVectorType(val elementType: PrimitiveType, val numElements: Int, override val name: String): NamedType()
object Vec2Type: PrimitiveVectorType(FloatType,2, "vec2")
object Vec3Type: PrimitiveVectorType(FloatType,3, "vec3")
object Vec4Type: PrimitiveVectorType(FloatType,4, "vec4")

abstract class PrimitiveMatrixType(val elementType: PrimitiveType, val numRows: Int, val numColumns: Int, override val name: String): NamedType()
object Mat2x2Type: PrimitiveMatrixType(FloatType,2,2,"mat2")
object Mat3x3Type: PrimitiveMatrixType(FloatType,3,3,"mat3")
object Mat4x4Type: PrimitiveMatrixType(FloatType,4,4,"mat4")
