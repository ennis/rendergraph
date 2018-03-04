package patapon.rendergraph.lang.declarations

import patapon.rendergraph.lang.diagnostics.DiagnosticSink
import patapon.rendergraph.lang.psi.RgModule
import patapon.rendergraph.lang.psi.RgModuleContents
import patapon.rendergraph.lang.resolve.DeclarationResolver
import patapon.rendergraph.lang.resolve.LazyScope
import patapon.rendergraph.lang.resolve.Scope
import patapon.rendergraph.lang.resolve.ScopeImpl

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
    override val members = LazyScope(this, module, declarationResolver, d)

    override val resolutionScope: Scope
        get() = members
}
