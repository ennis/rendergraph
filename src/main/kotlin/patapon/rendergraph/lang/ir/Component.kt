package patapon.rendergraph.lang.ir

import patapon.rendergraph.lang.psi.RgComponent

class Component(psi: RgComponent,
                override val containingDeclaration: Declaration,
                enclosingScope: LazyValue<Scope>): LazyDeclaration
{
    val members: LazyValue<Scope>
    override val name = psi.identifier.text!!
    override val attributes = emptyList<Attribute>()

    val bases = Lazy<List<Component>> {
        TODO("Resolve bases")
    }

    private fun resolveMembers(psi: RgComponent, enclosingScope: LazyValue<Scope>): Scope
    {
        return ScopeImpl(enclosingScope, this) {
            if (psi.constantList.isNotEmpty()) {
                TODO("Constants are not yet implemented")
            }
            if (psi.passList.isNotEmpty()) {
                TODO("Pass definitions are not yet implemented")
            }

            psi.functionList.map { psiFunction ->
                // at this time, `members` is still resolving, so it will produce an error if
                // Function tries to access it
                val f = Function(psiFunction, this@Component, members)
                addDeclaration(f)
            }
        }
    }

    override fun forceFullResolve() {
        //bases.doResolve()
        // The order of operations is important here
        members.value.getAllDeclarations().forEach { decl -> if (decl is LazyDeclaration) decl.forceFullResolve() }
    }

    init {
        members = Lazy {
            resolveMembers(psi, enclosingScope)
        }
    }
}


