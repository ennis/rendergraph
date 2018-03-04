package patapon.rendergraph.lang.resolve

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.PsiUtil
import patapon.rendergraph.lang.declarations.BindingContext
import patapon.rendergraph.lang.declarations.Declaration
import patapon.rendergraph.lang.declarations.DeclarationWithResolutionScope
import patapon.rendergraph.lang.declarations.VariableDeclaration
import patapon.rendergraph.lang.diagnostics.DiagnosticSink
import patapon.rendergraph.lang.diagnostics.error
import patapon.rendergraph.lang.psi.*
import patapon.rendergraph.lang.utils.memoize
import patapon.rendergraph.lang.utils.Lazy

// A scope whose elements (declarations) are resolved on-demand
class LazyScope(
        override val owningDeclaration: DeclarationWithResolutionScope,
        val scopeParentElement: PsiElement,
        val declarationResolver: DeclarationResolver,
        val d: DiagnosticSink
    ): Scope
{

    val decls = memoize(::resolve)
    val allDeclarations_ = Lazy {
        doGetAllDeclarations()
    }

    fun doGetAllDeclarations(): Collection<Declaration> {
        // just perform name resolution for each PSI declaration and collect the results
        // Note: this could be lazy as well
        return PsiTreeUtil.findChildrenOfType(scopeParentElement, RgNamedDeclaration::class.java).flatMap { o -> decls(o.name!!) }.distinct()
    }

    override fun getAllDeclarations(): Collection<Declaration> = allDeclarations_.value

    // Resolve a declaration by its name, regardless of its type
    // Also handles conflicting declarations within the same scope that cannot be disambiguated
    // name shadowing (redeclaration of a symbol in an enclosing scope) is not checked here
    // because the rules vary between different kinds of scopes
    private fun resolve(name: String): Collection<Declaration> {
        // find all PSI declaration elements that match by name
        val matching = PsiTreeUtil.findChildrenOfType(scopeParentElement, RgNamedDeclaration::class.java)
                .filter { it.name == name }
                .mapNotNull {
                    when (it)
                    {
                        is RgFunction -> declarationResolver.resolveFunctionDeclaration(it, owningDeclaration, this@LazyScope)
                        is RgComponent -> declarationResolver.resolveComponentDeclaration(it, owningDeclaration)
                        is RgVariable -> declarationResolver.resolveVariableDeclaration(it, owningDeclaration)
                        else -> null
                    }
                }

        // now check for name clashes
        matching.zip(matching).forEach { (a, b) ->
            if (a != b && a.name == b.name) {
                // TODO function overloading
                // CONTEXT(X.XX): redeclaration within the same member scope
                d.error("Conflicting declarations: ${a.name} and ${b.name}")
            }
        }

        return matching
    }

    override fun findDeclarations(name: String): Collection<Declaration> = decls(name)

    override fun getParentScope(): Scope? {
        return null
    }
}