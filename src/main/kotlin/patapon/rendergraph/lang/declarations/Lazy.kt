package patapon.rendergraph.lang.declarations

interface LazyValue<out T>
{
    val isResolved: Boolean
    val isResolving: Boolean
    val value: T
    fun doResolve()
}

abstract class LazyValueBase<out T>: LazyValue<T>
{
    protected var resolving = false
    override val isResolving get() = resolving
}

class Lazy<out T>(private val resolve: () -> T): LazyValueBase<T>() {
    private var _value: T? = null
    override val value: T
        get() {
            doResolve()
            return _value!!
        }

    override fun doResolve() {
        if (resolving) {
            throw IllegalStateException("Already resolving")
        }
        if (_value == null) {
            resolving = true
            _value = resolve()
            resolving = false
        }
    }

    override val isResolved get() = _value != null
}

class NullableLazy<out T>(private val resolve: () -> T?): LazyValueBase<T?>() {
    private var _value: T? = null
    private var _resolved = false
    override val value: T?
        get() {
            doResolve()
            return _value
        }

    override fun doResolve() {
        if (resolving) {
            throw IllegalStateException("Already resolving")
        }
        if (!_resolved) {
            resolving = true
            _value = resolve()
            _resolved = true
            resolving = false
        }
    }

    override val isResolved get() = _resolved
}

class NullLazy<out T>(): LazyValue<T?>
{
    override val isResolved = true
    override val isResolving = false
    override val value: T? = null
    override fun doResolve() {}
}