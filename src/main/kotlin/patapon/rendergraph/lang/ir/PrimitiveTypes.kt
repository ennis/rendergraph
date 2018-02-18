package patapon.rendergraph.lang.ir


interface PrimitiveType: Type
{
    val byteSize: Int
}

object FloatType : PrimitiveType
{
    override val name = "float"
    override val byteSize = 4
}

object IntegerType : PrimitiveType
{
    override val name = "int"
    override val byteSize = 4
}

object UnresolvedType: PrimitiveType
{
    override val name = "<unresolved>"
    override val byteSize = 0
}