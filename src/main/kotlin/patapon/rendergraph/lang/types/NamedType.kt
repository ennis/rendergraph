package patapon.rendergraph.lang.types

abstract class NamedType: Type {
    abstract val name: String
    override fun toString() = name
}
