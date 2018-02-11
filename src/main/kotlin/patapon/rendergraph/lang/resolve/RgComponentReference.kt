package patapon.rendergraph.lang.resolve

import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiPolyVariantReference
import com.intellij.psi.PsiReferenceBase
import patapon.rendergraph.lang.psi.RgComponent

class RgComponentReference(element: PsiElement, textRange: TextRange): PsiReferenceBase<PsiElement>(element, textRange)  {
    override fun resolve(): PsiElement? {
        val project = element.project
        TODO()
    }

    override fun getVariants(): Array<Any> {
        TODO("not implemented")
    }
}