package patapon.rendergraph.lang.declarations

import patapon.rendergraph.lang.psi.RgAttribute
import patapon.rendergraph.lang.resolve.Scope

interface Declaration
{
    val name: String
    val owningDeclaration: Declaration? get() = null

    fun forceFullResolve() {}
}
