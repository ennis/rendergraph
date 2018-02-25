package patapon.rendergraph.lang.declarations

import patapon.rendergraph.lang.psi.RgModule
import patapon.rendergraph.lang.psi.RgModuleContents
import patapon.rendergraph.lang.resolve.DeclarationResolver
import patapon.rendergraph.lang.resolve.Scope
import patapon.rendergraph.lang.resolve.ScopeImpl

interface ModuleDeclaration: DeclarationWithResolutionScope
{
    val members: Scope
}

class ModuleDeclarationImpl(
        override val name: String,
        val declarationResolver: DeclarationResolver,
        module: RgModule
    ): ModuleDeclaration
{
    override val members: Scope
        get() = members_.value

    val members_ = Lazy<Scope> {
        declarationResolver.resolveMemberScope(this, module.moduleContents!!)
    }

    override val resolutionScope: Scope
        get() = members
}
