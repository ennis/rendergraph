package patapon.rendergraph.lang.resolve

import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import patapon.rendergraph.lang.declarations.Declaration
import patapon.rendergraph.lang.declarations.DeclarationWithResolutionScope
import patapon.rendergraph.lang.diagnostics.DiagnosticSink
import patapon.rendergraph.lang.diagnostics.error
import patapon.rendergraph.lang.psi.*
import patapon.rendergraph.lang.utils.Lazy
import patapon.rendergraph.lang.utils.memoize

// A scope whose elements (declarations) are resolved on-demand
abstract class LazyMemberScope(
        val owningDeclaration: DeclarationWithResolutionScope,
        private val scopeParentElement: PsiElement,
        private val d: DiagnosticSink
) : Scope {
    private val decls = memoize(::resolve)
    private val allDeclarations = Lazy {
        doGetAllDeclarations()
    }

    private fun doGetAllDeclarations(): Collection<Declaration> {
        // just perform name resolution for each PSI declaration and collect the results
        // Note: this could be lazy as well
        return PsiTreeUtil.findChildrenOfType(scopeParentElement, RgNamedDeclaration::class.java).flatMap { o -> decls(o.name!!) }.distinct()
    }

    override fun getAllDeclarations(): Collection<Declaration> = allDeclarations.value

    // Resolve a declaration by its name, regardless of its type
    // Also handles conflicting declarations within the same scope that cannot be disambiguated
    // name shadowing (redeclaration of a symbol in an enclosing scope) is not checked here
    // because the rules vary between different kinds of scopes
    private fun resolve(name: String): Collection<Declaration> {
        // find all PSI declaration elements that match by name
        val matching = scopeParentElement.children.filterIsInstance(RgNamedDeclaration::class.java)
                .filter { it.name == name }
                .mapNotNull { resolveDeclaration(it) }

        // now check for name clashes
        matching.forEach { a ->
            matching.forEach { b ->
                if (a != b && a.name == b.name) {
                    // TODO function overloading
                    // CONTEXT(X.XX): redeclaration within the same member scope
                    d.error("Conflicting declarations: ${a.name} and ${b.name}")
                }
            }
        }

        return matching
    }

    protected abstract fun resolveDeclaration(o: RgDeclaration): Declaration?

    override fun findDeclarations(name: String): Collection<Declaration> = decls(name)
}