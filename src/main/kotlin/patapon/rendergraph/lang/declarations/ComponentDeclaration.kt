package patapon.rendergraph.lang.declarations

import com.intellij.openapi.diagnostic.Logger
import patapon.rendergraph.lang.resolve.ScopeImpl
import patapon.rendergraph.lang.psi.RgComponent
import patapon.rendergraph.lang.resolve.DeclarationResolver
import patapon.rendergraph.lang.resolve.Scope

interface ComponentDeclaration: DeclarationWithResolutionScope
{
    // the scope used to resolve the members of the components
    // includes declarations of the base class
    // val memberResolutionScope: Scope
    val members: Scope
}

class ComponentDeclarationImpl(
        override val owningDeclaration: Declaration,
        override val name: String,
        val declarationResolver: DeclarationResolver,
        component: RgComponent
    ): ComponentDeclaration
{
    companion object {
        var LOG = Logger.getInstance(ComponentDeclarationImpl::class.java)
    }

    // A scope provides 'Declarations' on demand
    // when a name is queried, the AST is visited
    override val resolutionScope: Scope
        get() = TODO("compute the member resolution scope")

    override val members: Scope
        get() = members_.value

    val members_ = Lazy {
        declarationResolver.resolveMemberScope(this, component)
    }
}


