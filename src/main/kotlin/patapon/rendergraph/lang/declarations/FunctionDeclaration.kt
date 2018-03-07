package patapon.rendergraph.lang.declarations

import patapon.rendergraph.lang.psi.RgFunction
import patapon.rendergraph.lang.resolve.DeclarationResolver
import patapon.rendergraph.lang.resolve.Scope
import patapon.rendergraph.lang.types.Type
import patapon.rendergraph.lang.types.TypeResolver
import patapon.rendergraph.lang.utils.Lazy


interface FunctionDeclaration : DeclarationWithResolutionScope {
    val returnType: Type
    val bodyResolutionScope: Scope
}

class FunctionDeclarationImpl(
        override val owningDeclaration: DeclarationWithResolutionScope,
        override val name: String,
        val declarationResolver: DeclarationResolver,
        val typeResolver: TypeResolver,
        function: RgFunction) : FunctionDeclaration {

    override val returnType: Type
        get() = _returnType.value

    private val _returnType = Lazy { typeResolver.checkFunctionReturnType(function, owningDeclaration.resolutionScope, owningDeclaration.resolutionScope) }

    override val bodyResolutionScope: Scope
        get() = _bodyResolutionScope.value

    private val _bodyResolutionScope = Lazy {
        declarationResolver.resolveFunctionBodyScope(this, function)
    }

    override val resolutionScope: Scope
        get() = bodyResolutionScope

    override fun forceFullResolve() {
        //_bodyResolutionScope.doResolve()
        _returnType.doResolve()
    }
}
