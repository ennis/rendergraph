package patapon.rendergraph.lang.declarations

import com.intellij.openapi.diagnostic.Logger
import patapon.rendergraph.lang.diagnostics.DiagnosticSink
import patapon.rendergraph.lang.psi.RgComponent
import patapon.rendergraph.lang.resolve.DeclarationResolver
import patapon.rendergraph.lang.resolve.LazyScope
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
        override val owningDeclaration: Declaration,
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
        get() = TODO("not implemented")

    override val resolutionScope: Scope
        get() = TODO("compute the member resolution scope")

    override val members = LazyScope(this, component, declarationResolver, d)

}


