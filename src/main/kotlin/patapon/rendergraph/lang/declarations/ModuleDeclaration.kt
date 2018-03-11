package patapon.rendergraph.lang.declarations

import patapon.rendergraph.lang.diagnostics.DiagnosticSink
import patapon.rendergraph.lang.psi.*
import patapon.rendergraph.lang.resolve.*
import patapon.rendergraph.lang.utils.Lazy

interface ModuleDeclaration: DeclarationWithResolutionScope
{
    val members: Scope
}

class ModuleDeclarationImpl(
        override val name: String,
        val declarationResolver: DeclarationResolver,
        module: RgModule,
        d: DiagnosticSink
    ): ModuleDeclaration
{
    override val resolutionScope: Scope
        get() = _resolutionScope.value

    // TODO chaining the primitive scope here is a hack
    val _resolutionScope = Lazy { ChainedScope(members, PrimitiveScope) }

    override val members = object : LazyMemberScope(this, module.moduleContents!!, d) {
        override fun resolveDeclaration(o: RgDeclaration): Declaration? {
            return when (o) {
                is RgFunction -> declarationResolver.resolveFunctionDeclaration(o, this@ModuleDeclarationImpl, resolutionScope)
                is RgComponent -> declarationResolver.resolveComponentDeclaration(o, this@ModuleDeclarationImpl)
                is RgVariable -> declarationResolver.resolveVariableDeclaration(o, this@ModuleDeclarationImpl, resolutionScope)
                else -> null
            }
        }
    }

    override fun forceFullResolve() {
        _resolutionScope.doResolve()
        members.getAllDeclarations().forceFullResolve()
    }
}
