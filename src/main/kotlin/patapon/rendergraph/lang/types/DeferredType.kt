package patapon.rendergraph.lang.types

import patapon.rendergraph.lang.utils.Lazy

class DeferredType(compute: () -> Type): WrappedType {
    private val typeLazy = Lazy { compute() }

    val isComputed = typeLazy.isResolved

    override fun toString(): String {
        return if (!isComputed) {
            "<not yet computed>"
        }
        else {
            "DeferredType(${typeLazy.value})"
        }
    }

    override fun unwrap(): UnwrappedType {
        // can nest several deferred types?
        return typeLazy.value.unwrap()
    }
}
