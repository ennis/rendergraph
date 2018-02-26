package patapon.rendergraph.lang.psi.impl

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import patapon.rendergraph.lang.psi.RgNamedElement

abstract class RgNamedElementImpl(node: ASTNode): ASTWrapperPsiElement(node), RgNamedElement {
    override fun getName(): String? {
        return nameIdentifier?.text ?: "<unnamed>"
    }

    override fun setName(name: String): PsiElement {
        TODO("not implemented")
    }
}
