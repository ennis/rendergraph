package patapon.rendergraph.lang.declarations

import patapon.rendergraph.lang.psi.RgFunction
import patapon.rendergraph.lang.resolve.DeclarationResolver
import patapon.rendergraph.lang.resolve.Scope
import patapon.rendergraph.lang.types.Type
import patapon.rendergraph.lang.types.TypeResolver
import patapon.rendergraph.lang.utils.Lazy


interface FunctionDeclaration : DeclarationWithResolutionScope {
    val returnType: Type
}

class FunctionDeclarationImpl(
        override val owningDeclaration: DeclarationWithResolutionScope,
        override val name: String,
        private val declarationResolver: DeclarationResolver,
        private val typeResolver: TypeResolver,
        private val function: RgFunction) : FunctionDeclaration {

    override val returnType: Type
        get() = _returnType.value

    private val _returnType = Lazy { typeResolver.checkFunctionReturnType(function, owningDeclaration.resolutionScope, owningDeclaration.resolutionScope) }

    //override val resolutionScope: Scope
    //    get() = bodyResolutionScope

    fun doResolveBody() {
        declarationResolver.resolveFunctionBody(function, this, owningDeclaration.resolutionScope)
    }

    override val resolutionScope: Scope
        get() = owningDeclaration.resolutionScope

    override fun forceFullResolve() {
        //_bodyResolutionScope.doResolve()
        doResolveBody()
        _returnType.doResolve()
    }
}
