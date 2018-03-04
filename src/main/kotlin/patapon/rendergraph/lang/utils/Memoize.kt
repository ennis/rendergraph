package patapon.rendergraph.lang.utils

class MemoizedFunction1<T, R>(val f: (T) -> R) : (T) -> R
{
    val cache = mutableMapOf<T,R>()

    // if f(p1) is null the result is not memoized!
    override fun invoke(p1: T): R = cache.computeIfAbsent(p1) { f(p1) }
}

fun<T,R> memoize(f: (T) -> R) = MemoizedFunction1(f)
