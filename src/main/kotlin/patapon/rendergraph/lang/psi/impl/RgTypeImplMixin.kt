package patapon.rendergraph.lang.psi.impl

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiReference
import patapon.rendergraph.lang.psi.RgType
import patapon.rendergraph.ide.references.RgTypeReference

abstract class RgTypeImplMixin(node: ASTNode): ASTWrapperPsiElement(node), RgType {
    override fun getReference(): PsiReference? {
        return RgTypeReference(this)
    }
}