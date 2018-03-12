package patapon.rendergraph.lang.declarations

import com.intellij.psi.PsiElement
import patapon.rendergraph.lang.psi.RgAttribute
import patapon.rendergraph.lang.resolve.Scope

interface Declaration
{
    val sourceElement: PsiElement?
    val name: String
    val owningDeclaration: Declaration? get() = null

    fun forceFullResolve() {}
}
