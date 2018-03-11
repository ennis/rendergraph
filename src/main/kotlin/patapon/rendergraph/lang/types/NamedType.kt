package patapon.rendergraph.lang.types

interface NamedType: Type
{
    val name: String
}

abstract class NamedTypeImpl(override val name: String): NamedType {
    override fun toString() = name
}
