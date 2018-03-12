package patapon.rendergraph.lang.psi.impl

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiReference
import patapon.rendergraph.lang.psi.RgPath
import patapon.rendergraph.lang.psi.RgType
import patapon.rendergraph.lang.resolve.RgTypeReference

abstract class RgTypeImplMixin(node: ASTNode): ASTWrapperPsiElement(node), RgType {
    override fun getReference(): PsiReference? {
        return RgTypeReference(this)
    }
}