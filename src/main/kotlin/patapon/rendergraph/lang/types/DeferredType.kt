package patapon.rendergraph.lang.types

import patapon.rendergraph.lang.utils.Lazy

class DeferredType(compute: () -> Type): Type {
    val typeLazy = Lazy { compute() }

    fun compute(): Type = typeLazy.value
    val isComputed = typeLazy.isResolved

    override fun toString(): String {
        return if (!isComputed) {
            "<not yet computed>"
        }
        else {
            "DeferredType(${typeLazy.value})"
        }
    }
}
