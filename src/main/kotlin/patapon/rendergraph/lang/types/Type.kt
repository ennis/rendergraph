package patapon.rendergraph.lang.types

interface Type {
    fun unwrap(): UnwrappedType
}

abstract class UnwrappedType: Type
{
    final override fun unwrap() = this
}

interface WrappedType: Type
