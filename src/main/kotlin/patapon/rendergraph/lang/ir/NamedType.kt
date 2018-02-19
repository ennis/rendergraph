package patapon.rendergraph.lang.ir

abstract class NamedType: Type {
    abstract val name: String
    override fun toString() = name
}
