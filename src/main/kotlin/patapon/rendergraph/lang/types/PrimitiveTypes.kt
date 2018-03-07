package patapon.rendergraph.lang.types

abstract class PrimitiveType(override val name: String): NamedType()

object FloatType : PrimitiveType("float")
object DoubleType : PrimitiveType("double")
object IntegerType :  PrimitiveType("int")
object UnitType : PrimitiveType("void")
object UnresolvedType: PrimitiveType("<unresolved>")
object BooleanType: PrimitiveType("bool")
// this type cannot be spelled: it has no declaration
object NothingType: PrimitiveType("Nothing")

abstract class PrimitiveVectorType(val elementType: PrimitiveType, val numElements: Int, override val name: String): NamedType()
object Vec2Type: PrimitiveVectorType(FloatType,2, "vec2")
object Vec3Type: PrimitiveVectorType(FloatType,3, "vec3")
object Vec4Type: PrimitiveVectorType(FloatType,4, "vec4")
