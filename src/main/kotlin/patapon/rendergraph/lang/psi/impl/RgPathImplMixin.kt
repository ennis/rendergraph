package patapon.rendergraph.lang.psi.impl

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference
import patapon.rendergraph.lang.psi.RgPath
import patapon.rendergraph.lang.resolve.RgPathReference

abstract class RgPathImplMixin(node: ASTNode): ASTWrapperPsiElement(node), RgPath
{
    override fun getReference(): PsiReference? {
        return RgPathReference(this, textRange)
    }
}