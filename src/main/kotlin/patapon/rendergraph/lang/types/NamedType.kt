package patapon.rendergraph.lang.types

import patapon.rendergraph.lang.types.Type

abstract class NamedType: Type {
    abstract val name: String
    override fun toString() = name
}
