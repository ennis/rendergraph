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

    val _resolutionScope = Lazy { ChainedScope(members) }

    override val members = object : LazyMemberScope(this, module, d) {
        override fun resolveDeclaration(o: RgDeclaration): Declaration? {
            return when (o) {
                is RgFunction -> declarationResolver.resolveFunctionDeclaration(o, this@ModuleDeclarationImpl, resolutionScope)
                is RgComponent -> declarationResolver.resolveComponentDeclaration(o, this@ModuleDeclarationImpl)
                is RgVariable -> declarationResolver.resolveVariableDeclaration(o, this@ModuleDeclarationImpl)
                else -> null
            }
        }
    }
}
