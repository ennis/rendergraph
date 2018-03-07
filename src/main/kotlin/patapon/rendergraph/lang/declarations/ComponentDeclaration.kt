package patapon.rendergraph.lang.declarations

import com.intellij.openapi.diagnostic.Logger
import patapon.rendergraph.lang.diagnostics.DiagnosticSink
import patapon.rendergraph.lang.psi.RgComponent
import patapon.rendergraph.lang.psi.RgDeclaration
import patapon.rendergraph.lang.psi.RgFunction
import patapon.rendergraph.lang.psi.RgVariable
import patapon.rendergraph.lang.resolve.ChainedScope
import patapon.rendergraph.lang.resolve.DeclarationResolver
import patapon.rendergraph.lang.resolve.LazyMemberScope
import patapon.rendergraph.lang.resolve.Scope
import patapon.rendergraph.lang.utils.Lazy

interface ComponentDeclaration: DeclarationWithResolutionScope
{
    // the scope used to resolve the members of the components
    // includes declarations of the base class
    // val memberResolutionScope: Scope

    //
    // component A {
    //      fun f
    // }
    //
    // component B: A {
    //      fun g
    // }
    //
    // A.inheritanceScope = { }
    // B.inheritanceScope = { f }
    // A.members = { f }
    // B.members = { g }
    // A.resolutionScope = { <lexical scope>, f }
    // B.resolutionScope = { <lexical scope>, f, g }

    // scope of all inherited declarations
    val inheritanceScope: Scope
    // scope of all members (without inherited declarations)
    val members: Scope
}

class ComponentDeclarationImpl(
        override val owningDeclaration: DeclarationWithResolutionScope,
        override val name: String,
        declarationResolver: DeclarationResolver,
        component: RgComponent,
        d: DiagnosticSink
    ): ComponentDeclaration
{
    companion object {
        var LOG = Logger.getInstance(ComponentDeclarationImpl::class.java)
    }

    override val inheritanceScope: Scope
        get() = members

    override val resolutionScope: Scope
        get() = _resolutionScope.value

    val _resolutionScope = Lazy { ChainedScope(inheritanceScope, owningDeclaration.resolutionScope) }

    override val members = object : LazyMemberScope(this, component, d) {
        override fun resolveDeclaration(o: RgDeclaration): Declaration? {
            return when (o) {
                is RgFunction -> declarationResolver.resolveFunctionDeclaration(o, this@ComponentDeclarationImpl, resolutionScope)
                is RgComponent -> declarationResolver.resolveComponentDeclaration(o, this@ComponentDeclarationImpl)
                is RgVariable -> declarationResolver.resolveVariableDeclaration(o, this@ComponentDeclarationImpl)
                else -> null
            }
        }
    }

}


